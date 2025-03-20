import {Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {MainComponent} from './main/main.component';
import {AccueilComponent} from './accueil/accueil.component';
import {SigninComponent} from './signin/signin.component';
import {ListeSuiviComponent} from './liste-suivi/liste-suivi.component';
import {ProfilComponent} from './profil/profil.component';
import {ArticleDetailsComponent} from './article-details/article-details.component';
import {AjoutArticle} from './ajoutArticle/ajoutArticle.component';
import {Guards} from './login/guards';

export const routes: Routes = [
  {path:'AjoutArticle',component:AjoutArticle, canActivate: [Guards]}, // rajouter canActivate: [Guards]
  {path: 'login', component: LoginComponent},
  {path : 'main', component: MainComponent, canActivate: [Guards]}, // rajouter canActivate: [Guards]
  {path: '', component: AccueilComponent},
  {path : 'signin', component: SigninComponent},
  {path: 'listeSuivi', component: ListeSuiviComponent, canActivate: [Guards]}, // rajouter canActivate: [Guards]
  {path: 'profil', component: ProfilComponent, canActivate: [Guards]}, // rajouter canActivate: [Guards]
  {path: 'article/:id', component: ArticleDetailsComponent, canActivate: [Guards]} // rajouter canActivate: [Guards]

];
