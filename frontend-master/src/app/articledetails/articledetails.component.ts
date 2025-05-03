import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticleService} from '../services/article.service';
import {Article} from '../classes/article';
import { CanvasJSAngularChartsModule} from '@canvasjs/angular-charts';
import {NgForOf, NgIf} from '@angular/common';
import { Subscription,Subject,forkJoin,interval,  takeUntil} from 'rxjs';
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

export class ArticleDetailsComponent implements OnInit{
  demarrage: Date = new Date();
  timerDemarre: boolean = false;
  private intervalSubscription?: Subscription;
  private destroy$ = new Subject<void>();
  article: Article = new Article(); //mettre constructeur vide quand acces a la base
  id: number = 0;
  afficherGraphique: boolean = false;
  filtre : string = 'tout' ; // en min pour l'instant
  articles: any[] = [];
  tempsDepuisDernUp = 0;
  tempsRestantEnSecondes: number | null = null;
  tempsRestant: string = 'En attente du démarrage...';
  timerSubscription?: Subscription;
  isDropdownVisible: boolean = true;
  constructor(private articleService: ArticleService,
              private route: ActivatedRoute,
              private router: Router,private UserService:UserService) {}

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
      this.article.updateNow = data.updateNow;
      console.log("data.derniereUpdate", data.derniereupdate);
      this.article.derniereUpdate = new Date(data.derniereupdate); // Assurez-vous que c'est une Date
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
                let prixStr = item.y;

                // Vérifie si le prix commence par un symbole euro
                if (prixStr.startsWith('€')) {
                  // Si le symbole euro est présent au début, on le retire
                  prixStr = prixStr.replace('€', '');
                }
                const prix = parseFloat(prixStr

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
        this.article.sites.forEach(s => {
          this.updateChartOptions(s.graph, this.article, s.graph.dataPoints[0].x);
        })
        console.log("article avant construiregraphgloaal: ", this.article);
        console.log("article.sites avant construiregraphgloaal: ", this.article.sites);
        this.construireGraphGlobal(this.article, this.article.sites);
        this.updateChartOptions(this.article.graph, this.article, this.article.graph.dataPoints[0].x);
        this.demarrage = this.article.derniereUpdate; // Initialiser la date de démarrage
        this.demarrerCompteARebours();
      }, error => console.log("Erreur getTendanceBySiteId", error));
    }, error => console.log("Erreur getArticleByID", error));
  }
  afficherTendancePrix() {
    this.afficherGraphique = true;
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
      updateNow: article.updateNow,  // Date de mise à jour actuelle
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

  deconnexion() {
    this. UserService.logout();

    return "ajout test";
  }

  changerFiltre(filtre: string, graph : Graphique) {
      this.filtre = filtre;
      const maintenant = new Date();
      let debutPeriode: Date | undefined;

      switch (filtre) {
        case 'heure':
          debutPeriode = new Date(maintenant.getFullYear(), maintenant.getMonth(), maintenant.getDate(), maintenant.getHours() - 1);
          break;
        case 'jour':
          debutPeriode = new Date(maintenant.getFullYear(), maintenant.getMonth(), maintenant.getDate());
          break;
        case 'semaine':
          debutPeriode = new Date(maintenant.getFullYear(), maintenant.getMonth(), maintenant.getDate() - 7);
          break;
        case 'mois':
          debutPeriode = new Date(maintenant.getFullYear(), maintenant.getMonth() - 1, maintenant.getDate());
          break;
        case 'semestre':
          debutPeriode = new Date(maintenant.getFullYear(), maintenant.getMonth() - 6, maintenant.getDate());
          break;
        case 'annee':
          debutPeriode = new Date(maintenant.getFullYear() - 1, maintenant.getMonth(), maintenant.getDate());
          break;
        case 'tout':
          debutPeriode = graph.dataPoints[0].x; // Pas de filtre
          break;
        default:
          debutPeriode = graph.dataPoints[0].x;
          break;
      }
      this.updateChartOptions(graph, this.article, debutPeriode);
  }

  updateChartOptions(graph : Graphique, article : Article, debutPeriode : Date): void {
    console.log("debut periode :" + debutPeriode);
    graph.options = {
      animationEnabled: true,
        zoomEnabled : true,
        data: [{
        type: 'line',
        dataPoints: graph.dataPoints,
        xValueType : 'DateTime'
      }],
        axisX : {
        title : 'Temps',
          labelAngle : 0,
          minimum : graph.dataPoints[0].x,
          interval : article.frequence,
          viewportMinimum : debutPeriode,
      },
      axisY : {
        title : 'Prix',
          suffix : ' €',
          stripLines:[{
            startValue: article.seuil,
            endValue: article.seuil - 1,
            color:'#d8d8d8'
        }]
      }
    }
  }

  forceUpdate(article : any) {
    article.updateNow = 1;
    this.demarrerCompteARebours();
    this.modifierArticle(this.article);
  }

  demarrerCompteARebours(): void {
    if (this.article.frequence > 0 && this.article.derniereUpdate) {
      const maintenantEnMs = Date.now();
      const derniereUpdateEnMs = this.article.derniereUpdate.getTime();
      this.tempsDepuisDernUp = Math.floor((maintenantEnMs - derniereUpdateEnMs) / 1000); // Différence en secondes

      this.tempsRestantEnSecondes = Math.max(0, Math.floor(this.article.frequence * 60 - this.tempsDepuisDernUp));
      this.mettreAJourAffichageTempsRestant();
      this.demarrerTimer();
      this.timerDemarre = true;
    } else {
      console.error('La fréquence de l\'article n\'est pas valide ou la date de dernière mise à jour est manquante.');
      this.tempsRestant = 'Erreur de démarrage du compte à rebours';
    }
  }

  private tempsEcouleSubscription?: Subscription;

  demarrerTimer(): void {
    this.timerSubscription = interval(1000)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        if (this.tempsRestantEnSecondes !== null && this.tempsRestantEnSecondes > 0) {
          this.tempsRestantEnSecondes--;
          this.mettreAJourAffichageTempsRestant();
          if (this.tempsRestantEnSecondes === 0) {
            console.log('Compte à rebours terminé !');
            // Ici, vous pouvez appeler la fonction pour forcer la mise à jour
            this.forceUpdate(this.article);
            this.arreterTimer(); // Arrêter le timer une fois terminé
          }
        } else if (this.tempsRestantEnSecondes === 0) {
          // Empêcher la décrémentation en dessous de zéro (sécurité)
          this.arreterTimer();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.intervalSubscription) {
      this.intervalSubscription.unsubscribe();
    }
    if (this.tempsEcouleSubscription) {
      this.tempsEcouleSubscription.unsubscribe();
    }
  }

  mettreAJourAffichageTempsRestant(): void {
    if (this.tempsRestantEnSecondes !== null) {
      const minutes = Math.floor(this.tempsRestantEnSecondes / 60);
      const secondes = this.tempsRestantEnSecondes % 60;
      this.tempsRestant = `${this.padZero(minutes)}:${this.padZero(secondes)}`;
    } else {
      this.tempsRestant = 'En attente du démarrage...';
    }
  }

  arreterTimer(): void {
    if (this.timerSubscription) {
      this.timerSubscription.unsubscribe();
      this.timerSubscription = undefined;
    }
  }

  padZero(nombre: number): string {
    return nombre < 10 ? '0' + nombre : '' + nombre;
  }

}