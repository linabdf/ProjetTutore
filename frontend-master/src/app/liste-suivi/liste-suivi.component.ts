import {Component} from '@angular/core';
import {NgForOf} from '@angular/common';
import {Article} from '../classes/article';
import {CanvasJSAngularChartsModule} from '@canvasjs/angular-charts';
import {ArticleService} from '../services/article.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-liste-suivi',
  imports: [
    NgForOf,
    CanvasJSAngularChartsModule
  ],
  templateUrl: './liste-suivi.component.html',
  styleUrl: './liste-suivi.component.css'
})

export class ListeSuiviComponent {
  articles: Article[] = [];
  chartOptions: any[] = [];

  constructor(private articleService: ArticleService, private router: Router,) {
  }

  ngOnInit() {
    this.articleService.getArticles().subscribe(articles => {
      this.articles = articles;
    });

  }
  afficherTendancePrix() {
    const btn = document.querySelectorAll(".btnTendancePrix") as NodeListOf<HTMLElement>;


    btn.forEach(element => {
      element.addEventListener("click", () => { // Arrow function to preserve 'this' context
        element.classList.toggle("active");
        const content = element.nextElementSibling as HTMLElement;

        if (content.style.display === 'none') {
          content.style.display = 'block';
        } else {
          content.style.display = 'none';
        }
      });
    });
  }

  modifierSeuil() {

  }

  supprimerArticleListe() {

  }

  afficherArticle(id: string) {
    this.router.navigate(['article', id]);
  }
}
