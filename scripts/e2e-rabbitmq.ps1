# End-to-end RabbitMQ stock validation via API Gateway (port 9000).
# Prerequisites: Eureka, Config (optional), Gateway, catalogue (8095), commandes (8098), RabbitMQ on 5672.
# Usage: .\e2e-rabbitmq.ps1
#        $env:GATEWAY = "http://localhost:9000"; .\e2e-rabbitmq.ps1

$ErrorActionPreference = "Stop"
$base = if ($env:GATEWAY) { $env:GATEWAY.TrimEnd("/") } else { "http://localhost:9000" }

function Invoke-CurlJson {
    param([string]$Method, [string]$Url, [string]$BodyPath = $null)
    $args = @("-s", "-w", "`nHTTP_STATUS:%{http_code}", "-X", $Method, "-H", "Content-Type: application/json", $Url)
    if ($BodyPath) { $args = @("-s", "-w", "`nHTTP_STATUS:%{http_code}", "-X", $Method, "-H", "Content-Type: application/json", "-d", "@$BodyPath", $Url) }
    $raw = & curl.exe @args
    if ($LASTEXITCODE -ne 0) { throw "curl failed ($Method $Url)" }
    $lines = $raw -split "`n"
    $statusLine = $lines | Where-Object { $_ -match "^HTTP_STATUS:" } | Select-Object -Last 1
    $code = [int]($statusLine -replace "HTTP_STATUS:", "")
    $jsonText = ($lines | Where-Object { $_ -notmatch "^HTTP_STATUS:" }) -join "`n"
    return @{ StatusCode = $code; Body = $jsonText }
}

$tmp = Join-Path $PSScriptRoot "e2e-tmp"
New-Item -ItemType Directory -Force -Path $tmp | Out-Null

Write-Host "== Gateway: $base ==" -ForegroundColor Cyan

# 1) Optional gateway health (add spring-boot-starter-actuator on apigateway to get 200)
$gwHealth = Invoke-CurlJson -Method "GET" -Url "$base/actuator/health"
Write-Host "GET $base/actuator/health -> $($gwHealth.StatusCode) (404 is OK if actuator absent)"

# 2) Categorie via gateway (canonical + plural alias check)
$catFile = Join-Path $tmp "cat.json"
$catJson = (@{ nom = "E2E Cat $(Get-Random)"; description = "e2e" } | ConvertTo-Json -Compress)
[System.IO.File]::WriteAllText($catFile, $catJson, [System.Text.UTF8Encoding]::new($false))

$rCat = Invoke-CurlJson -Method "POST" -Url "$base/categorie" -BodyPath $catFile
if ($rCat.StatusCode -notin @(200, 201)) {
    $rCat2 = Invoke-CurlJson -Method "POST" -Url "$base/categories" -BodyPath $catFile
    if ($rCat2.StatusCode -notin @(200, 201)) { throw "POST categorie failed: $($rCat.StatusCode) $($rCat.Body)" }
    $rCat = $rCat2
}
$catId = ($rCat.Body | ConvertFrom-Json).id
Write-Host "Created categorie id=$catId (POST /categorie or /categories)"

# 3) Produit via gateway (canonical + plural alias)
$prodFile = Join-Path $tmp "prod.json"
$prodPayload = @{
    nom            = "E2E Prod $(Get-Random)"
    description    = "e2e"
    prix           = 99.5
    quantiteStock  = 100
    vendeurId      = 1
    categorie      = @{ id = $catId }
    images         = @()
    caracteristiques = @{}
} | ConvertTo-Json -Depth 5 -Compress
[System.IO.File]::WriteAllText($prodFile, $prodPayload, [System.Text.UTF8Encoding]::new($false))

$rProd = Invoke-CurlJson -Method "POST" -Url "$base/produit" -BodyPath $prodFile
if ($rProd.StatusCode -notin @(200, 201)) {
    $rProd2 = Invoke-CurlJson -Method "POST" -Url "$base/produits" -BodyPath $prodFile
    if ($rProd2.StatusCode -notin @(200, 201)) { throw "POST produit failed: $($rProd.StatusCode) $($rProd.Body)" }
    $rProd = $rProd2
}
$prodId = ($rProd.Body | ConvertFrom-Json).id
$stockBefore = ($rProd.Body | ConvertFrom-Json).quantiteStock
Write-Host "Created produit id=$prodId stock=$stockBefore (POST /produit or /produits)"

# 4) Commande via gateway (canonical + singular alias)
$orderFile = Join-Path $tmp "order.json"
$orderPayload = @{
    clientId             = 42
    adresseLivraison     = "1 rue Test"
    dateLivraisonPrevue  = (Get-Date).ToString("yyyy-MM-dd")
    lignes               = @(
        @{ produitId = $prodId; quantite = 2; prixUnitaire = 99.5 }
    )
    livraison            = $null
} | ConvertTo-Json -Depth 6 -Compress
[System.IO.File]::WriteAllText($orderFile, $orderPayload, [System.Text.UTF8Encoding]::new($false))

$rOrder = Invoke-CurlJson -Method "POST" -Url "$base/commandes" -BodyPath $orderFile
if ($rOrder.StatusCode -notin @(200, 201)) {
    $rOrder2 = Invoke-CurlJson -Method "POST" -Url "$base/commande" -BodyPath $orderFile
    if ($rOrder2.StatusCode -notin @(200, 201)) { throw "POST commandes failed: $($rOrder.StatusCode) $($rOrder.Body)" }
    $rOrder = $rOrder2
}
$order = $rOrder.Body | ConvertFrom-Json
$orderId = $order.id
$lastOrder = $order
Write-Host "Created commande id=$orderId statut=$($order.statut) (POST /commandes or /commande)"

# 5) Poll until RabbitMQ validation completes
$deadline = (Get-Date).AddSeconds(30)
$finalStatut = $order.statut
while ((Get-Date) -lt $deadline) {
    Start-Sleep -Seconds 1
    $g = Invoke-CurlJson -Method "GET" -Url "$base/commandes/$orderId"
    if ($g.StatusCode -ne 200) { continue }
    $c = $g.Body | ConvertFrom-Json
    $lastOrder = $c
    $finalStatut = $c.statut
    if ($finalStatut -in @("VALIDEE", "ANNULEE")) { break }
}

Write-Host "Final commande statut: $finalStatut messageValidation=$($lastOrder.messageValidation)"

$rStock = Invoke-CurlJson -Method "GET" -Url "$base/produit/$prodId"
$stockAfter = ($rStock.Body | ConvertFrom-Json).quantiteStock
Write-Host "Produit $prodId stock before=$stockBefore after=$stockAfter"

if ($finalStatut -eq "VALIDEE" -and $stockAfter -eq ($stockBefore - 2)) {
    Write-Host "PASS: RabbitMQ flow OK (VALIDEE and stock -2)." -ForegroundColor Green
    exit 0
}
if ($finalStatut -eq "ANNULEE") {
    Write-Host "WARN: Commande ANNULEE (stock ou message catalogue). Vérifier RabbitMQ / logs catalogue." -ForegroundColor Yellow
    exit 2
}
Write-Host "FAIL: statut=$finalStatut ou stock inattendu (attendu VALIDEe + stock $($stockBefore - 2))." -ForegroundColor Red
exit 1
