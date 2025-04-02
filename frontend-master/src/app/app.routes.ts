import {Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {MainComponent} from './main/main.component';
import {AccueilComponent} from './accueil/accueil.component';
import {SigninComponent} from './signin/signin.component';
//import {ListeSuiviComponent} from './liste-suivi/liste-suivi.component';
import {ProfilComponent} from './profil/profil.component';
import {ArticleDetailsComponent} from './articledetails/articledetails.component';
import {AjoutArticle} from './ajoutArticle/ajoutArticle.component';

export const routes: Routes = [
  {path:'AjouterArticle',component:AjoutArticle},
  {path: 'login', component: LoginComponent},
  {path : 'main', component: MainComponent},
  {path: '', component: AccueilComponent}, // rajouter canActivate: [AuthGuard]
  {path : 'signin', component: SigninComponent},
 // {path: 'listeSuivi', component: ListeSuiviComponent}, // rajouter canActivate: [AuthGuard]
  {path: 'profil', component: ProfilComponent}, // rajouter canActivate: [AuthGuard]
  {path: 'article/:id', component: ArticleDetailsComponent}
   // rajouter canActivate: [AuthGuard]
  
];
