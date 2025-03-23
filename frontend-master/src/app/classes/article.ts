import {Graphique} from './graph';
import {Site} from './site';

export class Article {

  private _id: number = 0;
  private _nom: string = "";
  private _description: string = "";
  private _notif: string = "";
  private _seuil: number = 0; // seuil de l'article (que l'utilisateur choisi)
  private _frequence: number = 0; // fréquence du scrapping (que l'utilisateur choisi)
  private _urlimage:string = "";
  private _sites: Site[] = []; // liste des sites sur lesquels l'article est disponible
  private _graph: Graphique = new Graphique(); // graphique du meilleur prix global


  constructor() {
  }

  // setters et getters
  get id(): number {
    return this._id;
  }

  set id(value: number) {
    this._id = value;
  }
  get notif():String{
    return this._notif;
  }
 
  set notif(value: string) {
    this._notif = value;
  }
  get nom(): string {
    return this._nom;
  }
  set urlimage(value: string) {
    this._urlimage = value;
  }
  get urlimage(): string {
    return this._urlimage;
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

  get frequence(): number {
    return this._frequence;
  }

  set frequence(value: number) {
    this._frequence = value;
  }

  get sites(): Site[] {
    return this._sites;
  }

  set sites(value: Site[]) {
    this._sites = value;
  }

  get graph(): Graphique {
    return this._graph;
  }

  set graph(value: Graphique) {
    this._graph = value;
  }

}