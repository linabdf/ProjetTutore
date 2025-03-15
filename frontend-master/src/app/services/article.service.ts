import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs'; // Importez of et Observable
import {Article} from '../classes/article';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  constructor() { }

  getArticles(): Observable<Article[]> {
    const articles: Article[] = [new Article(), new Article()]
    //données écrites en dur
    articles[0].id = "A001";
    articles[0].nom = "Article 1";
    articles[0].description = "Je suis l'article 1";
    articles[0].seuil = 50;
    articles[0].sites[0].graph.nom = "graph test 1.1";
    articles[0].sites[0].graph.type = "line";
    articles[0].sites[0].graph.dataPoints = [{
      x: new Date(2025, 2, 23, 10, 30),
      y: 71
    }, {x: new Date(2025, 2, 23, 10, 40), y: 55}, {x: new Date(2025, 2, 23, 10, 50), y: 50}];
    articles[0].sites[1].graph.nom = "graph test 1.2";
    articles[0].sites[1].graph.type = "line";
    articles[0].sites[1].graph.dataPoints = [{
      x: new Date(2025, 2, 23, 10, 30),
      y: 30
    }, {x: new Date(2025, 2, 23, 10, 35), y: 45}, {x: new Date(2025, 2, 23, 10, 40), y: 60}];

    articles[1].id = "A002";
    articles[1].nom = "Article 2";
    articles[1].description = "Je suis l'article 2";
    articles[1].seuil = 180;
    articles[1].sites[0].graph.nom = "graph test 2.1";
    articles[1].sites[0].graph.type = "line";
    articles[1].sites[0].graph.dataPoints = [{x: 30, y: 12}, {x: 8, y: 48}]

    return of(articles); // Utilisez 'of' pour retourner un Observable
  }
}
