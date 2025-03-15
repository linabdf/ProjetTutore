import {Graphique} from './graph';
import {Site} from './site';


export class Article {
  //ok changer mettre 1 graph ici et un graph pour chaque site
  private _id: string = "A000";
  private _nom: string = "";
  private _description: string = "";
  private _seuil: number = 0;
  private _sites: Site[] = [];

  get sites(): Site[] {
    return this._sites;
  }

  constructor() {
  }

  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value;
  }

  get nom(): string {
    return this._nom;
  }

  set nom(value: string) {
    this._nom = value;
  }

  get description(): string {
    return this._description;
  }

  set description(value: string) {
    this._description = value;
  }

  get seuil(): number {
    return this._seuil;
  }

  set seuil(value: number) {
    this._seuil = value;
  }

  set sites(value: Site[]) {
    this._sites = value;
  }

  private _graph: Graphique = new Graphique(); // graphique du meilleur prix global

  get graph(): Graphique {
    return this._graph;
  }

  set graph(value: Graphique) {
    this._graph = value;
  }

}
