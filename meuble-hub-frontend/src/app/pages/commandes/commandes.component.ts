import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-commandes',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="data-view-container">
      <h2>Base de données : Commandes</h2>
      <p class="subtitle">Récupéré dynamiquement depuis <code>http://localhost:9000/commandes</code></p>
      
      <div *ngIf="loading" class="spinner"></div>
      
      <div *ngIf="!loading && data.length === 0" class="empty-state">
        Aucune commande trouvée en base de données.
      </div>

      <div class="orders-grid" *ngIf="data.length > 0">
        <div *ngFor="let cmd of data" class="order-card glass-panel">
          <div class="order-header">
            <h3>Commande #{{ cmd.numeroCommande || cmd.id || 'N/A' }}</h3>
            <span class="status-badge" [ngClass]="cmd.statut?.toLowerCase() || 'inconnu'">{{ cmd.statut || 'Nouveau' }}</span>
          </div>
          <div class="order-body">
            <p><strong>Date:</strong> {{ (cmd.dateCommande | date:'medium') || 'Non définie' }}</p>
            <p><strong>Adresse:</strong> {{ cmd.adresseLivraison || 'Magasin' }}</p>
            <p><strong>ID Client:</strong> {{ cmd.clientId || 'Visiteur' }}</p>
          </div>
          <div class="order-footer">
            <span class="total-label">Total TTC</span>
            <span class="total-amount text-gradient">{{ cmd.total | number:'1.2-2' }} €</span>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .data-view-container { animation: fadeIn 0.5s ease-out; }
    h2 { color: var(--primary); font-size: 2.5rem; margin-bottom: 0.5rem; }
    .subtitle { color: var(--text-muted); margin-bottom: 30px; }
    
    .orders-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 24px; }
    .order-card { padding: 20px; display: flex; flex-direction: column; gap: 16px; border-top: 3px solid var(--border-subtle); transition: transform 0.3s, border-color 0.3s; }
    .order-card:hover { transform: translateY(-5px); border-top-color: var(--primary); }
    
    .order-header { display: flex; justify-content: space-between; align-items: center; }
    .order-header h3 { font-size: 1.2rem; margin: 0; color: #fff; }
    .status-badge { padding: 4px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: bold; background: rgba(255,255,255,0.1); color: var(--text-muted); }
    
    .status-badge.livree, .status-badge.validee, .status-badge.terminee { background: rgba(161, 209, 123, 0.2); color: #a1d17b; border: 1px solid #a1d17b; }
    .status-badge.en_attente_validation, .status-badge.en_cours { background: rgba(255, 215, 0, 0.2); color: #ffd700; border: 1px solid #ffd700; }
    
    .order-body p { margin-bottom: 8px; font-size: 0.95rem; }
    .order-body strong { color: var(--primary); }
    
    .order-footer { display: flex; justify-content: space-between; align-items: center; padding-top: 16px; margin-top: auto; border-top: 1px solid rgba(255,255,255,0.05); }
    .total-label { font-size: 0.9rem; color: var(--text-muted); }
    .total-amount { font-size: 1.5rem; font-weight: bold; }
    .text-gradient { background: linear-gradient(90deg, var(--primary), #ffffff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
  `]
})
export class CommandesComponent implements OnInit {
  data: any[] = [];
  loading = true;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getCommandes().subscribe({
      next: (val) => { this.data = val; this.loading = false; },
      error: (err) => { 
        console.error("Erreur Backend", err); 
        this.data = [{ status: "Erreur de connexion Gateway", details: err.message }];
        this.loading = false; 
      }
    });
  }
}
