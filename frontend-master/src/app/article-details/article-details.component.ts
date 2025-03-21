import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticleService} from '../services/article.service';
import {Article} from '../classes/article';
import {CanvasJSAngularChartsModule} from '@canvasjs/angular-charts';
import {NgForOf, NgIf} from '@angular/common';
import {forkJoin} from 'rxjs';
import {Graphique} from '../classes/graph';
import {FormsModule} from '@angular/forms';
import {Site} from '../classes/site';

@Component({
  selector: 'app-article-details',
  imports: [
    CanvasJSAngularChartsModule,
    NgForOf,
    NgIf,
    FormsModule
  ],
  templateUrl: './article-details.component.html',
  styleUrl: './article-details.component.css'
})
export class ArticleDetailsComponent implements OnInit {
  article: Article = new Article(); //mettre constructeur vide quand acces a la base
  id: number = 0;
  filtre : number = 1000; // en min pour l'instant

  constructor(private articleService: ArticleService,
              private route: ActivatedRoute,
              private router: Router) {
  }

  // à l'initialisation obtenir le projet que l'on a demandé à modifier

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.articleService.getArticleByID(this.id).subscribe(data => {
      this.article.id = data.id;
      this.article.nom = data.nom;
      this.article.seuil = data.seuil;
      this.article.description = data.description;
      this.article.frequence = data.frequence;
      this.article.sites = data.sites;
      this.article.graph = new Graphique();
      console.log("this.articles.sites", this.article.sites);

      // Initialisation de s.graph pour chaque site
      this.article.sites.forEach(s => {
        console.log("sites forEach init", s);
        s.graph = new Graphique(); // Crée une instance de Graphique
      });

      const requests = this.article.sites.map(s => {
        return this.articleService.getTendanceBySiteId(s.numS);
      });

      forkJoin(requests).subscribe(results => {
        results.forEach((data, index) => {
          const s = this.article.sites[index];
          if (Array.isArray(data)) {
            data.forEach(item => {
              if (item && item.date && item.prix) {
                s.graph.dataPoints.push({
                  x: new Date(item.date),
                  y: Number(item.prix),
                  toolTipContent: `${Number(item.prix)} €, le ${new Date(item.date).getDate()}/${new Date(item.date).getDay()} à ${new Date(item.date).getHours()}h${new Date(item.date).getMinutes()}`,
                });
                console.log("s.graph.dps :", s.graph.dataPoints);
              } else {
                console.warn("Item invalide détecté:", item); // Affiche un avertissement pour les éléments invalides
              }
            });
          }
        });

        // Appel de construireGraphGlobal ici, après que les données sont disponibles
        console.log("article avant construiregraphgloaal: ", this.article);
        console.log("article.sites avant construiregraphgloaal: ", this.article.sites);
        this.construireGraphGlobal(this.article, this.article.sites);

      }, error => console.log("Erreur getTendanceBySiteId", error));
    }, error => console.log("Erreur getArticleByID", error));
  }

  afficherTendancePrix() {
    const btn = document.querySelectorAll(".btnTendancePrix") as NodeListOf<HTMLElement>;


    btn.forEach(element => {
      element.addEventListener("click", () => { // Arrow function to preserve 'this' context
        element.classList.toggle("active");
        const content = element.nextElementSibling as HTMLElement;

        if (content.style.display === 'none') {
          content.style.display = 'block';
          content.style.width = '100%';
        } else {
          content.style.display = 'none';
        }
      });
    });
  }

  construireGraphGlobal(article : Article, sites : Site[]): void {
    console.log("articles en parametre", article);
    console.log("sites de l'article", article.sites);
    console.log("sites de l'article", sites);

    const allDataPoints: { x: Date; y: number; nomGraphique: string }[] = [];

    article.sites.forEach(s => {
      console.log("sites of article", s);
      const dataPointsAvecNom = s.graph.dataPoints.map(point => ({
        ...point,
        nomGraphique: s.nomSite,
      }));
      allDataPoints.push(...dataPointsAvecNom);
    })

    const minYParX = this.trouverYMinParX(allDataPoints);

    article.graph.dataPoints = Object.keys(minYParX).map(xStr => ({
      x: new Date(xStr),
      y: minYParX[xStr].y,
      toolTipContent: `${minYParX[xStr].y} € sur ${minYParX[xStr].nomGraphique} le ${new Date(xStr).getDate()}/${new Date(xStr).getDay()} à ${new Date(xStr).getHours()}h${new Date(xStr).getMinutes()}`,
    }))

    console.log("construiregraphglobal :", article.graph.dataPoints);
  }

  // trouver le prix minium de chaque graphique de chaque site
  trouverYMinParX(dataPoints: { x: Date; y: number; nomGraphique: string }[]): {
    [x: string]: { y: number; nomGraphique: string }
  } {
    const minYParX: { [x: string]: { y: number; nomGraphique: string } } = {};

    for (const point of dataPoints) {
      const xStr = point.x.toISOString();
      if (minYParX[xStr] === undefined || point.y < minYParX[xStr].y) {
        minYParX[xStr] = {y: point.y, nomGraphique: point.nomGraphique};
      }
    }

    console.log("minYParX :", minYParX);
    return minYParX;
  }


  modifierSeuil() {
    const btn = document.querySelectorAll(".btnModifSeuil") as NodeListOf<HTMLElement>;

    btn.forEach(element => {
      element.addEventListener("click", () => { // Arrow function to preserve 'this' context
        element.classList.toggle("active");
        const content = document.querySelector(".divInputSeuil") as HTMLElement;

        if (content.style.display === 'none') {
          content.style.display = 'block';
          content.style.width = '100%';
        } else {
          content.style.display = 'none';
        }
      });
    });
  }

  submitSeuil() {

  }

  modifierFrequence() {

  }


}
