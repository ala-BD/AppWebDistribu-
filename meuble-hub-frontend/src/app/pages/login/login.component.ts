import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="login-container">
      <div class="glass-panel login-box">
        <h2>Se Connecter</h2>
        <p class="subtitle">Accédez à votre espace membre Meuble Hub.</p>

        <div class="form-group">
          <label>Email</label>
          <input type="email" [(ngModel)]="credentials.email" class="input-glass" placeholder="votre@email.com">
        </div>
        
        <div class="form-group">
          <label>Mot de passe</label>
          <input type="password" [(ngModel)]="credentials.password" class="input-glass" placeholder="••••••••">
        </div>

        <button class="btn-primary w-100 mt-4" (click)="signIn()">Connexion</button>
        
        <div class="separator">OU</div>
        
        <button class="btn-secondary w-100" routerLink="/register">Nouveau ? Créer un compte</button>
      </div>
    </div>
  `,
  styles: [`
    .login-container { display: flex; justify-content: center; padding: 60px 20px; animation: fadeIn 0.4s ease-out;}
    .login-box { width: 100%; max-width: 400px; padding: 40px; text-align: center; }
    h2 { color: var(--primary); margin-bottom: 10px; }
    .subtitle { color: var(--text-muted); margin-bottom: 30px; font-size: 0.9rem; }
    
    .separator { margin: 24px 0; color: var(--text-muted); font-size: 0.85rem; letter-spacing: 2px; position: relative; }
    .separator::before, .separator::after { content: ''; position: absolute; top: 50%; width: 40%; height: 1px; background: rgba(255,255,255,0.1); }
    .separator::before { left: 0; }
    .separator::after { right: 0; }
    
    .btn-secondary { background: rgba(255,255,255,0.05); color: #fff; border: 1px solid rgba(255,255,255,0.2); border-radius: 8px; padding: 12px 24px; font-weight: bold; cursor: pointer; transition: all 0.3s ease; }
    .btn-secondary:hover { background: rgba(255,255,255,0.1); border-color: var(--primary); }
  `]
})
export class LoginComponent {
  credentials = { email: '', password: '' };

  constructor(private apiService: ApiService) {}

  signIn() {
    alert("Fonction de connexion prête ! \nBientôt intégrée avec Keycloak (JWT) derrière l'API Gateway 🚀");
  }
}
