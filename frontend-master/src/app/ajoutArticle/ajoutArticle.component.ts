import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../signin/user/user.service';

@Component({
  selector: 'app-ajout-article',
  imports: [FormsModule],
  templateUrl: './ajoutArticle.component.html',
  styleUrls: ['./ajoutArticle.component.css'],
})
export class AjoutArticle {
  constructor(private router: Router, private userService: UserService) {}

  article = {
    nom: '',
    seuil: '',
    frequence: '',
    notif: '',
    selectedSites: {
      amazon: false,
      fnac: false,
      ldlc: false,
      rueducommerce: false,
      boulanger:false,
      leclerc:false,
      alternate:false,
      ebay:false,
      cdiscount:false,
      lego:false
    },
  };

  // Soumission de l'article avec les sites sélectionnés
  /*onSubmit() {
    console.log('Données envoyées:', this.article);

    this.userService.createArticle(this.article).subscribe(
      (response) => {
        console.log('Article créé avec succès:', response);
        alert('Article ajouté avec succès');
      },
      (error) => {
        console.error('Erreur lors de la création de l\'article:', error);
        alert('Erreur lors de l\'ajout de l\'article');
      }
    );
  }*/
    onSubmit() {
      // Créer l'objet requestBody
      const requestBody = {
        article: {
          nom: this.article.nom,
          seuil: this.article.seuil,
          frequence: this.article.frequence,
          notif: this.article.notif
        },
        selectedSites: this.article.selectedSites
      };
  
      console.log('Données envoyées:', requestBody);
  
      // Envoi de la requête au backend
      this.userService.addArticleWithSites(requestBody).subscribe(
        (response) => {
          console.log('Réponse du backend:', response);
          alert('Article ajouté avec succès');
        },
        (error) => {
          console.error('Erreur lors de la création de l\'article:', error);
          alert('Erreur lors de l\'ajout de l\'article');
        }
      );
      this.router.navigateByUrl('/main');
      
    }
    deconnexion() {
      this.userService.logout();
      
      return "ajout test";
    }
  }

