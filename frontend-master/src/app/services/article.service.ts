import {inject, Injectable} from '@angular/core';
import {catchError, Observable, of, throwError} from 'rxjs'; // Importez of et Observable
import {Article} from '../classes/article';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  http = inject(HttpClient);

  userUrl = 'http://localhost:8080/myArticles';
  articleUrl = 'http://localhost:8080/article';
  tendanceUrl = 'http://localhost:8080/tendance';

  constructor() { }

  getArticleByID(id : number) : Observable<any> {
    return this.http.get<any>(`${this.articleUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any) {
    console.error("Erreur lors de la récupération de l'article :", error);
    return throwError(() => "Erreur lors de la récupération de l'article.");
  }

  getTendanceBySiteId(id : number) : Observable<any[]> {
    return this.http.get<any[]>(`${this.tendanceUrl}/site/${id}`).pipe(
      catchError(this.handleError)
    )
  }
}
