
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
import {UserService} from '../signin/user/user.service';
@Component({
  selector: 'app-article-details',
  imports: [
    CanvasJSAngularChartsModule,
    NgForOf,
    NgIf,
    FormsModule
  ],
  templateUrl: './articledetails.component.html',
  styleUrl: './articledetails.component.css'
})
export class ArticleDetailsComponent implements OnInit {
  article: Article = new Article(); //mettre constructeur vide quand acces a la base
  id: number = 0;
  filtre : number = 1000; // en min pour l'instant
  articles: any[] = [];
  isDropdownVisible: boolean = true;
  constructor(private articleService: ArticleService,
              private route: ActivatedRoute,
              private router: Router,private UserService:UserService) {
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
      this.article.notif=data.notif;
      this.article.urlimage=data.urlimage;
      this.article.graph = new Graphique();
      console.log("this.articles.sites", this.article.sites);
      console.log("this.articles.urlimage", this.article.urlimage);
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
              // Vérification de la date et du prix
              if (item && item.x && item.y) {
                // Conversion de la date
                const date = new Date(item.x);  // Assure-toi que c'est une date valide
                
                // Vérifie si la date est valide
                if (isNaN(date.getTime())) {
                  console.warn("Date invalide détectée:", item.x);
                  return; // Si la date est invalide, on passe à l'élément suivant
                }
      
                const prix = parseFloat(item.y
                  .replace(/[^\d€,-\s]/g, '')  // Enlever tout sauf les chiffres, €, -, , et l'espace insécable
                  .replace('€', ',')           // Remplacer le symbole euro par une virgule
                  .replace(/\s/g, '')          // Enlever tous les espaces (y compris les espaces insécables)
                  .replace(',', '.'));
                // Vérifie si le prix est valide
                if (isNaN(prix)) {
                  console.warn("Prix invalide détecté:", item.y);
                  return; // Si le prix est invalide, on passe à l'élément suivant
                }
      
                // Recherche si un point existe déjà pour cette date
                const existingPoint = s.graph.dataPoints.find(p => p.x.getTime() === date.getTime());
      
                if (existingPoint) {
                  // Si un point existe déjà pour cette date, on compare et on garde le meilleur prix (le plus bas)
                  if (prix < existingPoint.y) {
                    existingPoint.y = prix; // On met à jour avec le meilleur prix
                    existingPoint.toolTipContent = `${prix} €, le ${date.getDate()}/${date.getMonth() + 1} à ${date.getHours().toString().padStart(2, '0')}h${date.getMinutes().toString().padStart(2, '0')}`;
                  }
                } else {
                  // Sinon, on ajoute un nouveau point pour cette date
                  s.graph.dataPoints.push({
                    x: date,
                    y: prix,
                    toolTipContent: `${prix} €, le ${date.getDate()}/${date.getMonth() + 1} à ${date.getHours().toString().padStart(2, '0')}h${date.getMinutes().toString().padStart(2, '0')}`,
                  });
                }
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
    });
  
    // Calculer le minimum des Y par X
    const minYParX = this.trouverYMinParX(allDataPoints);
  
    // Mettre à jour les dataPoints de l'article avec les données minimisées
    article.graph.dataPoints = Object.keys(minYParX).map(xStr => ({
      x: new Date(xStr),
      y: minYParX[xStr].y,
      toolTipContent: `${minYParX[xStr].y} € sur ${minYParX[xStr].nomGraphique} le ${new Date(xStr).getDate()}/${new Date(xStr).getMonth() + 1} à ${new Date(xStr).getHours()}h${new Date(xStr).getMinutes()}`,
    }));
  
    console.log("construireGraphGlobal :", article.graph.dataPoints);
  }
  trouverYMinParX(dataPoints: { x: Date; y: number; nomGraphique: string }[]): {
    [x: string]: { y: number; nomGraphique: string }
  } {
    const minYParX: { [x: string]: { y: number; nomGraphique: string } } = {};
  
    for (const point of dataPoints) {
      // Arrondir l'heure à la minute près
      const dateArrondie = new Date(point.x);
      dateArrondie.setSeconds(0, 0);
      const xStr = dateArrondie.toISOString(); // Utilisation de toISOString pour avoir un format cohérent
  
      // Si la clé n'existe pas encore ou si le y du point courant est plus petit que celui déjà stocké, on le met à jour
      if (!minYParX[xStr] || point.y < minYParX[xStr].y) {
        minYParX[xStr] = { y: point.y, nomGraphique: point.nomGraphique };
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
 // Cette méthode est utilisée dans le template pour vérifier l'état des sites
 siteNames(selectedSites: { [key: string]: boolean }): string[] {
  return Object.keys(selectedSites);  // Récupère toutes les clés (noms des sites) de l'objet selectedSites
}  
 // Méthode pour basculer la visibilité du dropdown
 toggleArticleSitesVisibility(article: any): void {
  article.isSitesVisible = !article.isSitesVisible;  // Inverse la visibilité des sites pour cet article
}
trackById(index: number, item: any): number {
  return item.id;  // Utilise l'ID de l'objet pour suivre l'élément de manière unique
}
getBestPriceDataPoints(data: any): any[] {
  let bestPrices: { [key: string]: any } = {};

  Object.keys(data).forEach(timestamp => {
    if (!bestPrices[timestamp] || data[timestamp].y < bestPrices[timestamp].y) {
      bestPrices[timestamp] = {
        x: new Date(timestamp),  // Convertir en objet Date
        y: data[timestamp].y,    // Garder le prix le plus bas
      };
    }
  });

  return Object.values(bestPrices);
}
modifierArticle(article: any) {
  // Créer l'objet mis à jour avec les valeurs actuelles de l'article
  const updatedArticle = {
    numa:article.id,
    nom: article.nom, 
    Description:article.Description, // Nom de l'article
    frequence: article.frequence,  // Fréquence sélectionnée
    seuil: article.seuil,  // Seuil sélectionné
    notif: article.notif,
      // Type de notification
    selectedSites: article.selectedSites  // Sites sélectionnés
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

}