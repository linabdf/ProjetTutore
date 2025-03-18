import {Graphique} from './graph';

export class Site {

  private _id: string = "S000";
  private _urlSite: string = "";
  private _urlArticle: string = "";
  private _nom: string = "";
  private _graph: Graphique = new Graphique();

  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value;
  }

  get urlSite(): string {
    return this._urlSite;
  }

  set urlSite(value: string) {
    this._urlSite = value;
  }

  get urlArticle(): string {
    return this._urlArticle;
  }

  set urlArticle(value: string) {
    this._urlArticle = value;
  }

  get nom(): string {
    return this._nom;
  }

  set nom(value: string) {
    this._nom = value;
  }

  get graph(): Graphique {
    return this._graph;
  }

  set graph(value: Graphique) {
    this._graph = value;
  }

}
