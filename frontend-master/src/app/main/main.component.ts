import { Component, OnInit } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {Router} from '@angular/router';
import {UserService} from '../signin/user/user.service';
import { CommonModule } from '@angular/common';
import { Notification } from '../signin/user/notification';
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
  unreadCount: number = 0;
 // Liste des notifications
  isNotificationsVisible: boolean = false;
  isDropdownVisible: boolean = true;
  notificationsVisible: boolean = false; // Par défaut, les notifications ne sont pas visibles
  notifications: any[] = []
  apiNotifications: String []=[];
  notificationsmessage:Notification[] = [];
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
    this.notificationService.getUnreadCount();
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
    this.notificationService.notificationCount$.subscribe(
      (count) => {
        this.unreadCount = count;
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




 // Fonction pour récupérer le message depuis l'API
 getMessageFromAPI(): void {
  const apiUrl = 'http://localhost:8080/notifications/message';  // URL de l'API backend

  fetch(apiUrl)
    .then((response) => response.text())  // On récupère la réponse comme texte
    .then((data) => {
      this.message = data; 
      console.log('Message reçu du backend :', this.message);  // Afficher dans la console
    })
    .catch((error) => {
      console.error('Erreur lors de la récupération du message', error);
    });
 
    
}
removeNotification(notification: string): void {
  this.supprimerNotification(notification)
    .then(msg => {
      console.log('✅ Réponse du backend:', msg);
      this.notifications = this.notifications.filter((notif) => notif !== notification);
    })
    .catch(err => console.error('❌ Erreur:', err));
}
supprimerNotification(message: string): Promise<string> {
  return fetch(`http://localhost:8080/notifications/supprimer`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({message: message })
  }).then(response => {
    if (!response.ok) {
      throw new Error('Erreur lors de la suppression');
    }
    return response.text(); // ou .json() selon la réponse du backend
  });
}
showNotifications() {
  // Ici tu peux récupérer les notifications depuis l'API
  this.getMyPushNotifications().subscribe((data) => {
    console.log('Données reçues depuis l\'API :', data);
    this.notificationsmessage= data;
    console.log('Notifications récupérées :', this. notificationsmessage);
     // Remplir la liste des notifications
  });

  // Affiche les notifications
  this.isNotificationsVisible = true;
}

// Méthode pour fermer les notifications
closeNotifications() {
  this.isNotificationsVisible = false;
}

// Exemple de méthode pour récupérer les notifications
getMyPushNotifications() {
  // Logique pour récupérer les notifications depuis l'API (similaire à ce que tu fais)
  return this.UserService.getMyPushNotifications();
}
markAllAsRead() {
  // Appelle le backend pour marquer toutes les notifications comme lues
  fetch('http://localhost:8080/notifications/markAllAsRead', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + this.UserService.getToken() // si tu utilises un token
    }
  })
  .then(response => {
    if (response.ok) {
      console.log('✅ Notifications marquées comme lues');
      this.notificationService.updateNotificationCount(0);
      this.unreadCount = 0;

    } else {
      console.error('❌ Erreur lors du marquage comme lu');
    }
  })
  .catch(error => {
    console.error('Erreur réseau', error);
  });
}

}