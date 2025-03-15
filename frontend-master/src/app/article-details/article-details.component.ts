import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticleService} from '../services/article.service';
import {Article} from '../classes/article';
import {CanvasJSAngularChartsModule} from '@canvasjs/angular-charts';
import {NgForOf} from '@angular/common';
import {Site} from '../classes/site';

@Component({
  selector: 'app-article-details',
  imports: [
    CanvasJSAngularChartsModule,
    NgForOf
  ],
  templateUrl: './article-details.component.html',
  styleUrl: './article-details.component.css'
})
export class ArticleDetailsComponent {
  article: Article = new Article(); //mettre constructeur vide quand acces a la base
  id: string = "";

  constructor(private articleService: ArticleService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  // à l'initialisation obtenir le projet que l'on a demandé à modifier

  ngOnInit(): void {
 /*   this.id = this.route.snapshot.params['id'];
    //hard coded (changer quand acces a la base)
    this.article.id = this.id;

    this.article.nom = "Article 1";
    this.article.description = "Je suis l'article 1";
    this.article.seuil = 50;
    this.article.sites = [new Site(), new Site()]
    this.article.sites[0].nom = "Amazon"; //todo : ajouter les autres attributs (c'est chiant sans la base de données)
    this.article.sites[1].nom = "FNAC";
    this.article.sites[0].graph.nom = "graph test 1.1";
    this.article.sites[0].graph.type = "line";
    this.article.sites[0].graph.dataPoints = [{
      x: new Date(2025, 2, 23, 10, 30),
      y: 71
    }, {x: new Date(2025, 2, 23, 10, 40), y: 55}, {x: new Date(2025, 2, 23, 10, 50), y: 50}];
    this.article.sites[1].graph.nom = "graph test 1.2";
    this.article.sites[1].graph.type = "line";
    this.article.sites[1].graph.dataPoints = [{
      x: new Date(2025, 2, 23, 10, 30),
      y: 30
    }, {x: new Date(2025, 2, 23, 10, 35), y: 45}, {x: new Date(2025, 2, 23, 10, 40), y: 60}];


    /*
    this.articleService.getArticleByID(this.id).subscribe(data => {
      console.log(this.id)
      this.article = data;
    }, error => console.log(error));
    */
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

}
