import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-utilisateurs',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="data-view-container">
      <h2>Base de données : Utilisateurs</h2>
      <p class="subtitle">Récupéré dynamiquement depuis <code>http://localhost:9000/api/users</code></p>
      
      <div *ngIf="loading" class="spinner"></div>

      <div class="users-grid" *ngIf="!loading">
        <div *ngFor="let usr of data" class="user-card glass-panel">
          <div class="user-avatar">
            {{ (usr.prenom?.charAt(0) || '') + (usr.nom?.charAt(0) || 'U') | uppercase }}
          </div>
          <div class="user-info">
            <h3>{{ usr.prenom || 'Nouveau' }} {{ usr.nom || 'Client' }}</h3>
            <p>{{ usr.email || 'Pas d\\'email' }}</p>
            <span class="role-badge">{{ usr.roles ? usr.roles[0] : 'USER' }}</span>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .data-view-container { animation: fadeIn 0.5s ease-out; }
    h2 { color: var(--primary); font-size: 2.5rem; margin-bottom: 0.5rem; }
    .subtitle { color: var(--text-muted); margin-bottom: 30px; }
    
    .users-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 24px; padding-bottom: 40px; }
    .user-card { display: flex; align-items: center; gap: 20px; padding: 24px; transition: transform 0.3s; }
    .user-card:hover { transform: translateY(-5px); box-shadow: 0 10px 40px rgba(0,0,0,0.5); border-color: rgba(203, 168, 124, 0.3); }
    
    .user-avatar { width: 64px; height: 64px; border-radius: 50%; background: linear-gradient(135deg, var(--primary), var(--primary-hover)); display: flex; align-items: center; justify-content: center; font-size: 1.5rem; font-weight: bold; color: #1a1c23; box-shadow: 0 4px 15px rgba(203, 168, 124, 0.4); }
    
    .user-info { display: flex; flex-direction: column; align-items: flex-start; }
    .user-info h3 { margin: 0 0 4px 0; font-size: 1.15rem; color: #fff; }
    .user-info p { margin: 0 0 12px 0; font-size: 0.85rem; color: var(--text-muted); word-break: break-all; }
    
    .role-badge { background: rgba(255,255,255,0.1); border: 1px solid rgba(255,255,255,0.2); border-radius: 4px; padding: 4px 8px; font-size: 0.75rem; font-weight: bold; letter-spacing: 1px; color: #fff; }
  `]
})
export class UtilisateursComponent implements OnInit {
  data: any[] = [];
  loading = true;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getUtilisateurs().subscribe({
      next: (val) => { this.data = Array.isArray(val) ? val : [val]; this.loading = false; },
      error: (err) => { 
        this.data = [{ status: "Erreur HTTP 500/403: Vérifiez le Token Keycloak ou l'état du microservice AppDist Backend", details: err.message }];
        this.loading = false; 
      }
    });
  }
}
