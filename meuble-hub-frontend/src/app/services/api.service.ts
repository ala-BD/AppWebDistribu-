import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';

export interface Product {
  id: number;
  nom: string;
  description: string;
  prix: number; // Modifié pour matcher le backend Java
  quantiteStock?: number; // << AJOUT RÉCEMMENT
  images?: string[]; // Au lieu de imageUrl, Spring envoie une liste
  // Dynamic fields
  prixFinal?: number;
  promotionDetails?: Promotion;
}

export interface Promotion {
  _id: string;
  produitId: number;
  pourcentageReduction: number;
  dateDebut: string;
  dateFin: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  // UNIQUE POINT OF ENTRY : L'API Gateway sur le port 9000
  private GATEWAY_URL = 'http://localhost:9000';

  constructor(private http: HttpClient) {}

  // Microservice Catalogue (via Gateway)
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.GATEWAY_URL}/produit`);
  }

  // Microservice Promotions (via Gateway)
  getPromotions(): Observable<Promotion[]> {
    return this.http.get<Promotion[]>(`${this.GATEWAY_URL}/promotions`);
  }

  // Microservice Commandes (via Gateway)
  getCommandes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.GATEWAY_URL}/commandes`);
  }

  // Microservice Utilisateurs Backend (via Gateway - /api/users)
  getUtilisateurs(): Observable<any[]> {
    return this.http.get<any[]>(`${this.GATEWAY_URL}/api/users`);
  }

  // Microservice Paiements Backend (via Gateway - /api/paiements)
  getPaiements(): Observable<any[]> {
    return this.http.get<any[]>(`${this.GATEWAY_URL}/api/paiements`);
  }

  // --- ACTIONS REALES (POST) ---

  createProduct(product: any): Observable<any> {
    return this.http.post<any>(`${this.GATEWAY_URL}/produit`, product);
  }

  createCommande(commande: any): Observable<any> {
    return this.http.post<any>(`${this.GATEWAY_URL}/commandes`, commande);
  }

  createPromotion(promotion: any): Observable<any> {
    return this.http.post<any>(`${this.GATEWAY_URL}/promotions`, promotion);
  }

  registerUser(user: any): Observable<any> {
    return this.http.post<any>(`${this.GATEWAY_URL}/api/users`, user);
  }

  // Fonction architecturale incroyable : Joindre le Catalogue et les Promotions !
  getProductsWithPromotions(): Observable<Product[]> {
    return forkJoin({
      products: this.getProducts(),
      promotions: this.getPromotions()
    }).pipe(
      map(data => {
        const { products, promotions } = data;
        
        return products.map(product => {
          // Normaliser les types en chaîne de caractères pour s'assurer que '10' == '10' (Long vs Number vs String)
          const activePromo = promotions.find(p => String(p.produitId) === String(product.id));
          
          let finalPrice = product.prix || 0;
          
          if (activePromo) {
            const discount = (finalPrice * activePromo.pourcentageReduction) / 100;
            finalPrice = finalPrice - discount;
            product.promotionDetails = activePromo;
          }
          
          product.prixFinal = finalPrice;
          
          // Récupérer la première image de la liste envoyée par Java
          if(!product.images || product.images.length === 0) {
            product.images = ["https://images.unsplash.com/photo-1555041469-a586c61ea9bc?auto=format&fit=crop&w=600&q=80"];
          }
          
          return product;
        });
      })
    );
  }
}
