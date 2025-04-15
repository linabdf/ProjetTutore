import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainComponent } from './main/main.component';
import { AccueilComponent } from './accueil/accueil.component';
import { SigninComponent } from './signin/signin.component';
import { ProfilComponent } from './profil/profil.component';
import { ArticleDetailsComponent } from './articledetails/articledetails.component';
import { AjoutArticle } from './ajoutArticle/ajoutArticle.component';
import { AuthGuard } from './signin/user/AuthGuard';

export const routes: Routes = [
  { path: 'AjouterArticle', component: AjoutArticle, canActivate: [AuthGuard] },  // Protéger l'ajout d'articles
  { path: 'login', component: LoginComponent },  // Pas besoin de guard pour la page de connexion
  { path: 'main', component: MainComponent, canActivate: [AuthGuard] },  // Protéger la page principale
  { path: '', component: AccueilComponent },  // Accueil n'a pas de guard (public)
  { path: 'signin', component: SigninComponent },  // Page de sign-in n'a pas de guard
  { path: 'profil', component: ProfilComponent, canActivate: [AuthGuard] },  // Protéger le profil utilisateur
  { path: 'article/:id', component: ArticleDetailsComponent, canActivate: [AuthGuard] } ,
   // Protéger les détails d'articles
];