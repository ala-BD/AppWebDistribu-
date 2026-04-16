import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Promotion } from '../../services/api.service';

@Component({
  selector: 'app-promotions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="promos-container">
      <div class="promos-header">
        <h2>Gestion des Promotions (MongoDB)</h2>
        <p>Gérez vos remises en temps réel. Elles s'appliqueront instantanément au catalogue de MySQL !</p>
        <button class="btn-primary mt-4" (click)="toggleAddForm()">
          {{ showAddForm ? 'Fermer' : '+ Ajouter une Promotion' }}
        </button>
      </div>

      <!-- Add Promotion Form -->
      <div *ngIf="showAddForm" class="add-promo-form glass-panel">
        <h3>Lancer une Nouvelle Remise</h3>
        
        <div class="form-row">
          <div class="form-group">
            <label>Sélectionner le Produit concerné</label>
            <select [(ngModel)]="newPromo.produitId" class="input-glass">
              <option [ngValue]="null" disabled selected>-- Choisissez un meuble --</option>
              <option *ngFor="let p of catalogueProducts" [ngValue]="p.id">
                {{ p.nom }} (ID: {{ p.id }}) - Réf. Stock: {{ p.quantiteStock }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>% de Réduction</label>
            <input type="number" [(ngModel)]="newPromo.pourcentageReduction" class="input-glass" placeholder="ex: 20">
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Date Début</label>
            <input type="datetime-local" [(ngModel)]="newPromo.dateDebut" class="input-glass">
          </div>
          <div class="form-group">
            <label>Date Fin</label>
            <input type="datetime-local" [(ngModel)]="newPromo.dateFin" class="input-glass">
          </div>
        </div>

        <button class="btn-primary w-100 mt-4" (click)="submitPromotion()">Appliquer la Promo (POST /promotions)</button>
      </div>

      <div *ngIf="loading" class="spinner"></div>

      <div class="promos-grid" *ngIf="!loading">
        <div *ngFor="let promo of promotions" class="promo-card glass-panel">
          <div class="promo-header">
            <div>
              <p class="promo-title">Produit en promotion :</p>
              <h3>{{ getProductName(promo.produitId) }}</h3>
            </div>
            <span class="discount-badge">-{{ promo.pourcentageReduction }}%</span>
          </div>
          <div class="promo-body">
            <p><strong>Début :</strong> {{ (promo.dateDebut | date:'short') || 'Maintenant' }}</p>
            <p><strong>Fin :</strong> {{ (promo.dateFin | date:'short') || 'Jamais' }}</p>
            <p class="mongodb-id">MongoDB ID: {{ promo._id }}</p>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .promos-container { padding: 40px 20px; animation: fadeIn 0.5s ease-out; }
    h2 { color: var(--primary); font-size: 2.5rem; margin-bottom: 0.5rem; }
    .promos-header { text-align: center; margin-bottom: 50px; }
    
    .add-promo-form { padding: 30px; margin-bottom: 40px; border-left: 4px solid var(--primary); max-width: 600px; margin-left: auto; margin-right: auto; }
    
    .promos-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 24px; padding-bottom: 40px; }
    .promo-card { padding: 24px; border-top: 3px solid #ef4444; transition: transform 0.3s; display: flex; flex-direction: column; }
    .promo-card:hover { transform: translateY(-5px); border-top-color: var(--primary); box-shadow: 0 10px 40px rgba(239, 68, 68, 0.2); }
    
    .promo-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 16px; }
    .promo-title { font-size: 0.8rem; color: #ef4444; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; margin: 0 0 4px 0; }
    .promo-header h3 { margin: 0; font-size: 1.2rem; color: #fff; line-height: 1.3; }
    .discount-badge { background: #ef4444; color: white; padding: 6px 12px; border-radius: 20px; font-weight: bold; font-size: 1.2rem; box-shadow: 0 0 10px rgba(239, 68, 68, 0.5); }
    
    .promo-body p { margin-bottom: 8px; font-size: 0.95rem; color: var(--text-muted); }
    .promo-body strong { color: #e2e4e9; }
    .mongodb-id { font-family: monospace; font-size: 0.75rem !important; margin-top: 16px !important; color: #6b7280 !important; }
    
    select.input-glass option { background-color: #1a1c23; color: white; }
  `]
})
export class PromotionsComponent implements OnInit {
  promotions: Promotion[] = [];
  catalogueProducts: any[] = [];
  loading = true;
  showAddForm = false;

  newPromo = {
    produitId: null,
    pourcentageReduction: null,
    dateDebut: new Date().toISOString().slice(0,16),
    dateFin: new Date(Date.now() + 86400000 * 7).toISOString().slice(0,16) // +7 jours
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.fetchPromotions();
  }

  fetchPromotions() {
    this.loading = true;
    this.apiService.getPromotions().subscribe({
      next: (val) => { 
        this.promotions = val; 
        this.fetchCatalogue(); // Fetch des produits pour la liste déroulante
      },
      error: (err) => { console.error(err); this.loading = false; }
    });
  }

  fetchCatalogue() {
    this.apiService.getProductsWithPromotions().subscribe({
      next: (prods) => {
        this.catalogueProducts = prods;
        this.loading = false;
      },
      error: (err) => {
        console.error("Impossible de charger les noms des produits", err);
        this.loading = false;
      }
    });
  }

  toggleAddForm() {
    this.showAddForm = !this.showAddForm;
  }

  getProductName(produitId: number): string {
    if (!this.catalogueProducts || this.catalogueProducts.length === 0) return `Produit #${produitId}`;
    const product = this.catalogueProducts.find(p => p.id === produitId);
    return product ? `${product.nom}` : `Produit #${produitId}`;
  }

  submitPromotion() {
    if(!this.newPromo.produitId || !this.newPromo.pourcentageReduction) {
      alert("⚠️ Sélectionnez un Réf. Produit dans la liste et entrez une Réduction !");
      return;
    }

    this.apiService.createPromotion(this.newPromo).subscribe({
      next: () => {
        alert("🎉 Promotion magique injectée dans MongoDB avec succès ! Allez voir le catalogue dynamique !");
        this.showAddForm = false;
        this.fetchPromotions();
      },
      error: (err) => alert("❌ Erreur MongoDB : " + err.message)
    });
  }
}
