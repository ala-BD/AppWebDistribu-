import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="register-container">
      <div class="glass-panel register-box">
        <h2>Créer un Compte</h2>
        <p class="subtitle">Rejoignez Meuble Hub et commandez vos meubles.</p>

        <div class="form-row">
          <div class="form-group">
            <label>Prénom</label>
            <input type="text" [(ngModel)]="user.prenom" class="input-glass" placeholder="Jean">
          </div>
          <div class="form-group">
            <label>Nom</label>
            <input type="text" [(ngModel)]="user.nom" class="input-glass" placeholder="Dupont">
          </div>
        </div>

        <div class="form-group">
          <label>Email</label>
          <input type="email" [(ngModel)]="user.email" class="input-glass" placeholder="votre@email.com">
        </div>
        
        <div class="form-group">
          <label>Mot de passe</label>
          <input type="password" [(ngModel)]="user.password" class="input-glass" placeholder="••••••••">
        </div>

        <button class="btn-primary w-100 mt-4" (click)="signUp()">S'inscrire</button>
        <p style="margin-top:20px; font-size:0.9rem;">
          Déjà un compte ? <a routerLink="/login" style="color:var(--primary); cursor:pointer;">Se connecter</a>
        </p>
      </div>
    </div>
  `,
  styles: [`
    .register-container { display: flex; justify-content: center; padding: 60px 20px; }
    .register-box { width: 100%; max-width: 400px; padding: 40px; text-align: center; }
    h2 { color: var(--primary); margin-bottom: 10px; }
    .subtitle { color: var(--text-muted); margin-bottom: 30px; font-size: 0.9rem; }
  `]
})
export class RegisterComponent {
  user = { email: '', password: '', nom: '', prenom: '', role: 'CLIENT' };

  constructor(private apiService: ApiService) {}

  signUp() {
    this.apiService.registerUser(this.user).subscribe({
      next: (res) => alert("✅ Utilisateur créé avec succès dans AppDist Backend !"),
      error: (err) => alert("❌ Erreur de création du compte : " + err.message)
    });
  }
}
