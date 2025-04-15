import { Component, OnInit } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {Router} from '@angular/router';
import {UserService} from '../signin/user/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../notification/notification.service';

@Component({
  selector: 'app-main',
  standalone: true,  
  imports: [
  CommonModule, FormsModule
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {
  constructor(private router: Router,private UserService:UserService,  private notificationService: NotificationService
    ) {}
  message: string = '';
  articles: any[] = [];
  isDropdownVisible: boolean = true;
  notificationsVisible: boolean = false; // Par défaut, les notifications ne sont pas visibles
  notifications: any[] = []
 
  ajouterArticle() {
    this.router.navigateByUrl('/AjouterArticle');
    
    return "ajout test";
  }
  deconnexion() {
    console.log("casse toi")
    this.UserService.logout();
     
    return "ajout test";
  }
  ngOnInit() {
    console.log('ngOnInit appelé');
     
    if (this.UserService.isAuthenticated()) {
    this.UserService.getMyArticles().subscribe(
      (data) => {
        this.articles = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des articles :', error);
      }
    );
  }
    console.log('Component initialisé');
    this.notificationService.startSseConnection();
    console.log('fin ');
    this.notificationService.notifications$.subscribe(
      (message) => {
        this.notifications.push(message);
        if (this.notifications.length > 4) {
          this.notifications.shift();
        }
      }
    );
  
}
subscribeToNotifications():void {
  this.notificationService.notifications$.subscribe((message: string) => {
    this.notifications.unshift(message); // ajoute au début de la liste
  });
}
// Cette méthode est utilisée dans le template pour vérifier l'état des sites
 siteNames(selectedSites: { [key: string]: boolean }): string[] {
  return Object.keys(selectedSites);  // Récupère toutes les clés (noms des sites) de l'objet selectedSites
}  
 // Méthode pour basculer la visibilité du dropdown
 toggleArticleSitesVisibility(article: any): void {
  article.isSitesVisible = !article.isSitesVisible;  // Inverse la visibilité des sites pour cet article
}
modifierArticle(article: any) {
  // Créer l'objet mis à jour avec les valeurs actuelles de l'article
  const updatedArticle = {
    nom: article.nom, 
    Description:article.Description, // Nom de l'article
    frequence: article.frequence,  // Fréquence sélectionnée
    seuil: article.seuil,  // Seuil sélectionné
    notif: article.notif,
      // Type de notification
    selectedSites: article.selectedSites  // Sites sélectionnés
    // 
    };
  console.log('Données envoyées au backend:', updatedArticle);
  // Appel du service pour mettre à jour l'article dans le backend
  this.UserService.updateArticle(updatedArticle).subscribe(
    (response) => {
      console.log('Article mis à jour avec succès:', response);
      // Mettre à jour l'article dans le tableau des articles
      const index = this.articles.findIndex(a => a.numA === article.numA);
      if (index !== -1) {
        this.articles[index] = response; // Remplacer l'article modifié
      }
    },
    (error) => {
      console.error('Erreur lors de la mise à jour de l\'article:', error);
    }
  );
}
SupprimeArticle(article: any) {

  this.UserService.supprimerArticle(article).subscribe(
    (response) => {
      console.log('Article supprimé avec succès:', response);
      // Mettre à jour la liste des articles après suppression
      const index = this.articles.findIndex(a => a.nom === article.nom);
      if (index !== -1) {
        this.articles.splice(index, 1);  // Supprimer l'article de la liste
      }
    },
    (error) => {
      console.error('Erreur lors de la suppression de l\'article:', error);
    }
  );
}
voirArticle(id:string) {
  this.router.navigate(['article', id]);
 
}

toggleNotifications(): void {
  this.notificationsVisible = !this.notificationsVisible; // Toggle pour afficher/masquer les notifications
}



showNotifications(): void {
  console.log("Affichage des notifications :");
  this.notifications.forEach(notification => {
    console.log(notification);
  });
}
 // Fonction pour récupérer le message depuis l'API
 getMessageFromAPI(): void {
  const apiUrl = 'http://localhost:8080/notifications/message';  // URL de l'API backend

  fetch(apiUrl)
    .then((response) => response.text())  // On récupère la réponse comme texte
    .then((data) => {
      this.message = data;  // Stocker le message reçu
      console.log('Message reçu du backend :', this.message);  // Afficher dans la console
    })
    .catch((error) => {
      console.error('Erreur lors de la récupération du message', error);
    });
 
    
}}