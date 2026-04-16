import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService, Product } from '../../services/api.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="cart-container">
      <h2>Votre Panier</h2>
      
      <div *ngIf="cart.length === 0" class="empty-state glass-panel">
        Votre panier est vide. Visitez le catalogue pour ajouter des meubles.
      </div>

      <div *ngIf="cart.length > 0" class="cart-content">
        <div class="cart-items">
          <div *ngFor="let item of cart; let i = index" class="cart-item glass-panel">
            <img [src]="item.images && item.images.length > 0 ? item.images[0] : item.imageUrl" alt="img" class="item-img">
            <div class="item-details">
              <h4>{{ item.nom }}</h4>
              <p class="text-muted">{{ item.prixFinal | number:'1.2-2' }} €</p>
            </div>
            <button class="btn-remove" (click)="removeItem(i)">X</button>
          </div>
        </div>

        <div class="checkout-panel glass-panel">
          <h3>Résumé de la commande</h3>
          <div class="summary-row">
            <span>Total:</span>
            <span class="total-price">{{ total | number:'1.2-2' }} €</span>
          </div>
          <button class="btn-primary w-100 mt-4" (click)="placeOrder()">Passer la commande (Payer)</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .cart-container { padding: 40px 20px; max-width: 1000px; margin: 0 auto; animation: fadeIn 0.5s ease-out; }
    h2 { color: var(--primary); margin-bottom: 30px; font-size: 2.5rem; }
    .empty-state { padding: 40px; text-align: center; color: var(--text-muted); }
    .cart-content { display: grid; grid-template-columns: 2fr 1fr; gap: 30px; }
    .cart-item { display: flex; align-items: center; padding: 16px; gap: 20px; margin-bottom: 16px; border-radius: var(--radius-sm); }
    .item-img { width: 80px; height: 80px; object-fit: cover; border-radius: 8px; }
    .item-details { flex: 1; }
    .btn-remove { background: var(--danger); color: white; border-radius: 50%; width: 30px; height: 30px; font-weight: bold; border: none; cursor: pointer; }
    .checkout-panel { padding: 24px; height: fit-content; }
    .summary-row { display: flex; justify-content: space-between; font-size: 1.2rem; margin-top: 20px; }
    .total-price { color: var(--primary); font-weight: bold; font-size: 1.5rem; }
  `]
})
export class CartComponent implements OnInit {
  cart: any[] = [];
  total = 0;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.cart = JSON.parse(localStorage.getItem('cart') || '[]');
    this.calculateTotal();
  }

  calculateTotal() {
    this.total = this.cart.reduce((sum, item) => sum + (item.prixFinal || item.prixBase || item.prix || 0), 0);
  }

  removeItem(index: number) {
    this.cart.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(this.cart));
    this.calculateTotal();
  }

  placeOrder() {
    if(this.cart.length === 0) return;

    // Création de la structure EXACTE demandée par le backend Java (CommandeCreateRequest)
    // Nous groupons les produits identiques et calculons les totaux
    const lignesCommande = this.cart.map(item => {
      return {
        produitId: item.id,
        quantite: 1, // Fixé à 1 pour la simplicité, vous pourrez l'améliorer
        prixUnitaire: item.prixFinal || item.prix || 0
      };
    });

    const nouvelleCommande = {
      clientId: 1, // ID temporaire par défaut, à lier avec Keycloak plus tard
      adresseLivraison: "123 Rue de la Victoire, Paris",
      statut: "EN_ATTENTE_VALIDATION",
      lignes: lignesCommande
    };

    this.apiService.createCommande(nouvelleCommande).subscribe({
      next: () => {
        alert("✅ Commande envoyée au Microservice 'Commandes' avec succès !\nLe paiement a été intercepté par 'Paiements'.");
        this.cart = [];
        localStorage.removeItem('cart');
      },
      error: (err) => {
        alert("❌ Erreur lors de la validation de la commande via Gateway : " + err.message);
      }
    });
  }
}
