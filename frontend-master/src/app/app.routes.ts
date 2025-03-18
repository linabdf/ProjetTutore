import {Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {MainComponent} from './main/main.component';
import {AccueilComponent} from './accueil/accueil.component';
import {SigninComponent} from './signin/signin.component';
import {ListeSuiviComponent} from './liste-suivi/liste-suivi.component';
import {ProfilComponent} from './profil/profil.component';
import {ArticleDetailsComponent} from './article-details/article-details.component';
import {AjoutArticle} from './ajoutArticle/ajoutArticle.component';

export const routes: Routes = [
  {path:'AjoutArticle',component:AjoutArticle}, // rajouter canActivate: [Guards]
  {path: 'login', component: LoginComponent},
  {path : 'main', component: MainComponent}, // rajouter canActivate: [Guards]
  {path: '', component: AccueilComponent},
  {path : 'signin', component: SigninComponent},
  {path: 'listeSuivi', component: ListeSuiviComponent}, // rajouter canActivate: [Guards]
  {path: 'profil', component: ProfilComponent}, // rajouter canActivate: [Guards]
  {path: 'article/:id', component: ArticleDetailsComponent} // rajouter canActivate: [Guards]

];
