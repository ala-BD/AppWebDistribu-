import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Product } from '../../services/api.service';

@Component({
  selector: 'app-catalogue',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './catalogue.component.html',
  styleUrl: './catalogue.component.css'
})
export class CatalogueComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  
  // Feature: Ajouter un Produit
  showAddForm = false;
  newProduct: any = {
    nom: '',
    description: '',
    prix: 100,
    quantiteStock: 10,
    images: ['https://images.unsplash.com/photo-1540574163026-643ea20ade25?auto=format&fit=crop&w=600&q=80']
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.fetchProducts();
  }

  fetchProducts() {
    this.loading = true;
    this.apiService.getProductsWithPromotions().subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement via Gateway', err);
        // Fallback for demonstration aesthetics if Gateway is offline
        this.products = [
          { id: 1, nom: 'Canapé Velvet Royal', description: 'Un confort luxueux en velours. Mock Data.', prix: 1200, prixFinal: 1200, images: ['https://images.unsplash.com/photo-1555041469-a586c61ea9bc?auto=format&fit=crop&w=600&q=80'] },
          { id: 101, nom: 'Table Basse OAK', description: 'Bois de chêne massif.', prix: 400, prixFinal: 320, promotionDetails: { _id: 'X', produitId: 101, pourcentageReduction: 20, dateDebut: '', dateFin: '' }, images: ['https://images.unsplash.com/photo-1533090481720-856c6e3c1fdc?auto=format&fit=crop&w=600&q=80'] }
        ];
        this.loading = false;
      }
    });
  }

  toggleAddForm() {
    this.showAddForm = !this.showAddForm;
  }

  submitProduct() {
    this.apiService.createProduct(this.newProduct).subscribe({
      next: (res) => {
        alert("🎉 Produit ajouté avec succès dans MySQL !");
        this.showAddForm = false;
        this.fetchProducts(); // Refresh la liste
      },
      error: (err) => alert("❌ Erreur lors de la création : " + err.message)
    });
  }

  addToCart(product: Product) {
    // Simulation simple de panier via LocalStorage
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    cart.push(product);
    localStorage.setItem('cart', JSON.stringify(cart));
    alert(product.nom + " a été ajouté au panier !");
  }
}
