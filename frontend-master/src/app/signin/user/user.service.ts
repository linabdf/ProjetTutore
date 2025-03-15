import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';
import{Article} from'../../classes/article';
@Injectable({
  providedIn: 'root',
})
export class UserService {

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
          alert('Erreur de connexion : ' + data.message);
        }

        observer.next(data);
        observer.complete();
      })
      .catch(error => {
        console.error('Error:', error);
        observer.error(error);
      });
    });
  }/*
  createArticle(article: any): Observable<any> {
    console.log('article a envoyer',article);
    const token = this.getToken();
    console.log('token',token);
    return new Observable((observer)=>{
      fetch(`${this.articlebaseUrl}/addArticle`,{
        method:'POST',
        headers:{'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`},
      body:JSON.stringify(article),
      }).then((response)=>{
        if(response.ok){
          return response.json();
        }else{
          throw new Error('erreur lors de l\'envoie de l\'article');
        }
      })
      .then((data) => {
        console.log('Réponse de l\'API :', data); // Afficher la réponse de l'API
        observer.next(data); // Passer la réponse à l'observateur
        observer.complete(); // Terminer l'observateur
      })
      .catch((error) => {
        console.error('Erreur :', error); // Afficher l'erreur dans la console
        observer.error(error); // Passer l'erreur à l'observateur
      });
  });
}*/
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
  localStorage.removeItem('token');
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
  /*public addSiteToArticle(articleId: string, site: any): Observable<any> {
    return new Observable((observer) => {
      const token = localStorage.getItem('token'); // Récupérer le token
  
      if (!token) {
        observer.error('Aucun token trouvé. Veuillez vous connecter.');
        return;
      }
  
      fetch(`${this.articlebaseUrl}/articles/${articleId}/addSite`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(site), // Convertir l'objet site en JSON
      })
        .then(async (response) => {
          if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erreur ${response.status} : ${errorText}`);
          }
          return response.json();
        })
        .then((data) => {
          console.log(`✅ Site ajouté à l'article ${articleId} :`, data);
          observer.next(data);  // Envoie la réponse aux abonnés
          observer.complete();  // Termine l'observable
        })
        .catch((error) => {
          console.error('❌ Erreur lors de l\'ajout du site à l\'article :', error);
          observer.error(error);
        });
    });
  }
*/  
}



