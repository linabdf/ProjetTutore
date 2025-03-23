import { Injectable } from '@angular/core';
import { Observable, from } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  userUrl = 'http://localhost:8080/myArticles';
 private articleUrl :string='http://localhost:8080/article'; 
  tendanceUrl = 'http://localhost:8080/tendance';


  constructor() { }

  getArticleByID(id: number): Observable<any> {
    return from(
      fetch(`${this.articleUrl}/${id}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }).then(response => {
        if (!response.ok) {
          throw new Error(`Erreur HTTP: ${response.status}`);
        }
        return response.json();
      })
    ).pipe(
      catchError(error => {
        console.error("Erreur lors de la récupération de l'article :", error);
        throw error;
      })
    );
  }
  getSiteByID(id: number): Observable<any> {
    return from(
      fetch((`${this.articleUrl}/${id}`), {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }).then(response => {
        if (!response.ok) {
          throw new Error(`Erreur HTTP: ${response.status}`);
        }
        return response.json();
      })
    ).pipe(
      catchError(error => {
        console.error("Erreur lors de la récupération de l'article :", error);
        throw error;
      })
    );
  }

  getTendanceBySiteId(id: number): Observable<any[]> {
    return from(
      fetch(`${this.tendanceUrl}/site/${id}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }).then(response => {
        if (!response.ok) {
          throw new Error(`Erreur HTTP: ${response.status}`);
        }
        return response.json();
      })
    ).pipe(
      catchError(error => {
        console.error("Erreur lors de la récupération de l'article :", error);
        throw error; // Relance proprement l'erreur
      })
    );
  }
  
 
}
