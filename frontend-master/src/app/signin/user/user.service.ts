import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';
import{Article} from'../../classes/article';
import { Router, RouterModule } from '@angular/router';
@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private router: Router) {}
  private baseUrl: string = 'http://localhost:8080/auth'; 
  private articlebaseUrl: string = 'http://localhost:8080/article'; 
  public createUser(user: User): Observable<User> {
    console.log('User data being sent:', user);  // Ajoute cette ligne pour voir l'objet avant envoi
  
    return new Observable((observer) => {
      fetch(this.baseUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(user), // Sérialisation des données de l'utilisateur
      })
      .then(response => response.json()) // Conversion de la réponse en JSON
      .then(data => {
        console.log('Response from API:', data); // Affichage de la réponse de l'API
        alert(data.message);
        observer.next(data);
        observer.complete();
      })
      .catch(error => {
        console.error('Error:', error); // Affichage de l'erreur dans la console
        observer.error(error);
      });
    });
  }
  public loginUser(user: { email: string, password: string }): Observable<any> {
    console.log('Login data being sent:', user);  
    return new Observable((observer) => {
      fetch(`${this.baseUrl}/login`, {  
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),  
      })
      .then(response => response.json())  
      .then(data => {
        console.log('Response from API:', data);  
        if (data.token) {
          localStorage.setItem('token', data.token); 
          alert('Connexion réussie!');
        } else {
          alert('Connexion échoué ,verifiez votre email et  votre mot de passe');
        }

        observer.next(data);
        observer.complete();
      })
      .catch(error => {
        console.error('Error:', error);
        observer.error(error);
      });
    });
  }
public addArticleWithSites(requestBody: any): Observable<any> {
  return new Observable((observer) => {
    const token = localStorage.getItem('token'); // Récupérer le token d'authentification
    
    if (!token) {
      observer.error('Aucun token trouvé. Veuillez vous connecter.');
      return;
    }

    // URL de l'API pour ajouter un site à un article
    const url = `${this.articlebaseUrl}/addSite`;

    // Configuration des options de la requête
    const options: RequestInit = {
      method: 'POST', // Méthode POST pour ajouter un site à l'article
      headers: {
        'Authorization': `Bearer ${token}`, // En-tête d'authentification avec le token
        'Content-Type': 'application/json', // Spécifie que les données envoyées sont en JSON
      },
      body: JSON.stringify(requestBody), // Convertir le `requestBody` en chaîne JSON
    };

    // Effectuer la requête avec `fetch`
    fetch(url, options)
      .then(async (response) => {
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Erreur ${response.status}: ${errorText}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log('Article et sites ajoutés avec succès:', data);
        observer.next(data); // Envoie la réponse aux abonnés
        observer.complete(); // Terminer l'observable
      })
      .catch((error) => {
        console.error('Erreur lors de l\'ajout de l\'article et des sites:', error);
        observer.error(error); // Envoie l'erreur aux abonnés
      });
  });
  
}
 // ✅ Déconnexion (supprime le token)
public logout(): void {
  console.log("Déconnexion lancée...");
  const token = localStorage.getItem('token'); // Récupérer le token depuis le localStorage
  console.log("token",token)
  if (token) {
    // URL de l'API pour déconnecter l'utilisateur
    const url = `${this. baseUrl}/logout`;

    // Configuration des options pour la requête fetch
    const options: RequestInit = {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`, // En-tête d'authentification avec le token
        'Content-Type': 'application/json'
      }
    };

    // Effectuer la requête avec fetch pour déconnecter l'utilisateur
    fetch(url, options)
      .then(response => {
        if (!response.ok) {
          throw new Error('Erreur lors de la déconnexion');
        }
        // Supprimer le token du localStorage après une réponse réussie
        localStorage.removeItem('token');
        // Rediriger vers la page de connexion
        this.router.navigateByUrl('/login');
      })
      .catch(error => {
        console.error('Erreur lors de la déconnexion:', error);
      });
  } else {
    console.log("Aucun token trouvé, redirection vers la page de connexion...");
    // Si aucun token n'est trouvé, on redirige quand même
    this.router.navigateByUrl('/login');
  }
}
  // ✅ Récupérer le token
  public getToken(): string | null {
    return localStorage.getItem('token');
    
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token !== null;
  }

 
  public getMyArticles(): Observable<any> {
    return new Observable((observer) => {
      const token = localStorage.getItem('token');
      console.log(token); // Récupérer le token
      
      if (!token) {
        observer.error('Aucun token trouvé. Veuillez vous connecter.');
        return;
      }

      fetch(`${this.articlebaseUrl}/myArticles`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error('Erreur lors de la récupération des articles.');
          }
          return response.json();
        })
        .then((data) => {
          console.log('Articles récupérés :', data);
          observer.next(data);
          observer.complete();
        })
        .catch((error) => {
          console.error('Erreur :', error);
          observer.error(error);
        });
    });
    
  }
  
public updateArticle( updatedArticle: any): Observable<any> {
  return new Observable((observer) => {
    const token = localStorage.getItem('token'); // Récupérer le token d'authentification
    
    if (!token) {
      observer.error('Aucun token trouvé. Veuillez vous connecter.');
      return;
    }

    // URL pour mettre à jour l'article (avec son ID)
    const url = `${this.articlebaseUrl}/updateArticle`;

    // Configuration des options de la requête PUT
    const options: RequestInit = {
      method: 'PUT', // Méthode PUT pour modifier l'article
      headers: {
        'Authorization': `Bearer ${token}`, // En-tête d'authentification avec le token
        'Content-Type': 'application/json', // Spécifie que les données envoyées sont en JSON
      },
      body: JSON.stringify(updatedArticle), // Données mises à jour sous forme de JSON
    };

    // Effectuer la requête avec `fetch`
    fetch(url, options)
      .then(async (response) => {
      
  
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Erreur ${response.status}: ${errorText}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log('Article modifié avec succès:', data);
      window.location.reload();
        observer.next(data); // Envoie la réponse aux abonnés
        observer.complete();   // Terminer l'observable
      })
      .catch((error) => {
        console.error('Erreur lors de la modification de l\'article:', error);
        observer.error(error); // Envoie l'erreur aux abonnés
      });
  });
}
public supprimerArticle(article: any): Observable<any> {
  return new Observable((observer) => {
    const token = localStorage.getItem('token'); // Récupérer le token d'authentification

    if (!token) {
      observer.error('Aucun token trouvé. Veuillez vous connecter.');
      return;
    }

    // URL pour supprimer l'article en passant le nom dans l'URL
    const url = `${this.articlebaseUrl}/supprimerArticle/${article.nom}`;

    // Configuration des options de la requête DELETE
    const options: RequestInit = {
      method: 'DELETE', // Méthode DELETE pour supprimer l'article
      headers: {
        'Authorization': `Bearer ${token}`, // En-tête d'authentification avec le token
        'Content-Type': 'application/json', // Spécifie que le corps est en JSON, bien que ce ne soit pas nécessaire pour DELETE
      },
    };

    // Effectuer la requête avec `fetch`
    fetch(url, options)
      .then(async (response) => {
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Erreur ${response.status}: ${errorText}`);
        }
        return response.json();
      })
      .then((data) => {
        console.log('Article supprimé avec succès:', data);
        // Vous pouvez recharger la page ou mettre à jour la liste d'articles ici.
        window.location.reload(); // Recharger la page après suppression
        observer.next(data); // Envoie la réponse aux abonnés
        observer.complete();   // Terminer l'observable
      })
      .catch((error) => {
        console.error('Erreur lors de la suppression de l\'article:', error);
        observer.error(error); // Envoie l'erreur aux abonnés
      });
  });
}
public getMyPushNotifications(): Observable<any> {
  return new Observable((observer) => {
    const token = localStorage.getItem('token');
    console.log('🔐 Token utilisé :', token);

    if (!token) {
      observer.error('Aucun token trouvé. Veuillez vous connecter.');
      return;
    }

    fetch(`http://localhost:8080/article/utilisateur/push`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error('Erreur lors de la récupération des notifications.');
        }
        return response.json();
      })
      .then((data) => {
        console.log('🔔 data:', data);
        observer.next(data);
        observer.complete();
      })
      .catch((error) => {
        console.error('❌ Erreur de récupération :', error);
        observer.error(error);
      });
  });
}

}




