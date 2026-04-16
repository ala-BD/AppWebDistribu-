import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { CatalogueComponent } from './pages/catalogue/catalogue.component';
import { PromotionsComponent } from './pages/promotions/promotions.component';
import { CommandesComponent } from './pages/commandes/commandes.component';
import { UtilisateursComponent } from './pages/utilisateurs/utilisateurs.component';
import { PaiementsComponent } from './pages/paiements/paiements.component';
import { CartComponent } from './pages/cart/cart.component';
import { RegisterComponent } from './pages/register/register.component';
import { LoginComponent } from './pages/login/login.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'catalogue', component: CatalogueComponent },
  { path: 'promotions', component: PromotionsComponent },
  { path: 'commandes', component: CommandesComponent },
  { path: 'utilisateurs', component: UtilisateursComponent },
  { path: 'paiements', component: PaiementsComponent },
  { path: 'cart', component: CartComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent }
];
