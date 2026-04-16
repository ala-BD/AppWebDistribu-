import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-paiements',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="data-view-container">
      <h2>Base de données : Paiements</h2>
      <p class="subtitle">Récupéré dynamiquement depuis <code>http://localhost:9000/api/paiements</code></p>
      
      <div *ngIf="loading" class="spinner"></div>

      <div class="payments-wrapper glass-panel" *ngIf="!loading">
        <table class="payments-table">
          <thead>
            <tr>
              <th>ID Paiement</th>
              <th>Date</th>
              <th>Commande Réf</th>
              <th>Méthode</th>
              <th>Statut</th>
              <th class="text-right">Montant</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let p of data" class="payment-row">
              <td>#{{ p.transactionId || p.id || 'N/A' }}</td>
              <td>{{ (p.datePaiement | date:'short') || '---' }}</td>
              <td>Cmd #{{ p.commandeId || '---' }}</td>
              <td><span class="method-tag">{{ p.methode || 'N/A' }}</span></td>
              <td><span class="status-dot" [class.success]="p.statut === 'VALIDE'" [class.pending]="p.statut === 'EN_ATTENTE'"></span> {{ p.statut || 'Erreur' }}</td>
              <td class="text-right amount-cell">{{ p.montant | number:'1.2-2' }} €</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [`
    .data-view-container { animation: fadeIn 0.5s ease-out; }
    h2 { color: var(--primary); font-size: 2.5rem; margin-bottom: 0.5rem; }
    .subtitle { color: var(--text-muted); margin-bottom: 30px; }
    
    .payments-wrapper { padding: 24px; overflow-x: auto; }
    .payments-table { width: 100%; border-collapse: collapse; text-align: left; }
    .payments-table th { padding: 16px; color: var(--text-muted); font-size: 0.85rem; text-transform: uppercase; letter-spacing: 1px; border-bottom: 1px solid rgba(255,255,255,0.08); }
    .payments-table td { padding: 16px; border-bottom: 1px solid rgba(255,255,255,0.03); color: #e2e4e9; }
    
    .payment-row:hover td { background-color: rgba(203, 168, 124, 0.05); }
    
    .method-tag { background: rgba(255,255,255,0.1); padding: 4px 8px; border-radius: 4px; font-size: 0.85rem; font-weight: 600; }
    .text-right { text-align: right; }
    .amount-cell { color: #fff; font-weight: bold; font-family: monospace; font-size: 1.1rem; }
    
    .status-dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; background: #ef4444; margin-right: 6px; box-shadow: 0 0 8px #ef4444; }
    .status-dot.success { background: #10b981; box-shadow: 0 0 8px #10b981; }
    .status-dot.pending { background: #f59e0b; box-shadow: 0 0 8px #f59e0b; }
  `]
})
export class PaiementsComponent implements OnInit {
  data: any[] = [];
  loading = true;

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getPaiements().subscribe({
      next: (val) => { this.data = Array.isArray(val) ? val : [val]; this.loading = false; },
      error: (err) => { 
        this.data = [{ status: "Erreur - Paiements", details: err.message }];
        this.loading = false; 
      }
    });
  }
}
