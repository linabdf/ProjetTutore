import {Component} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {Article} from '../classes/article';
import {CanvasJSAngularChartsModule} from '@canvasjs/angular-charts';
import {ArticleService} from '../services/article.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-liste-suivi',
  imports: [
    NgForOf,
    CanvasJSAngularChartsModule,
    NgIf
  ],
  templateUrl: './liste-suivi.component.html',
  styleUrl: './liste-suivi.component.css'
})

export class ListeSuiviComponent {
  articles: Article[] = [];
  chartOptions: any[] = [];

  constructor(private articleService: ArticleService, private router: Router,) {
  }
/*
  ngOnInit() {
    this.articleService.getArticles().subscribe(articles => {
      this.articles = articles;
    });

  }
*/
  supprimerArticleListe() {

  }

  afficherArticle(id: number) {
    this.router.navigate(['article', id]);
  }
}
