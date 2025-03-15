import {Graphique} from './graph';

export class Site {

  private _id: string = "S000";

  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value;
  }

  private _urlSite: string = "";

  get urlSite(): string {
    return this._urlSite;
  }

  set urlSite(value: string) {
    this._urlSite = value;
  }

  private _urlArticle: string = "";

  get urlArticle(): string {
    return this._urlArticle;
  }

  set urlArticle(value: string) {
    this._urlArticle = value;
  }

  private _nom: string = "";

  get nom(): string {
    return this._nom;
  }

  set nom(value: string) {
    this._nom = value;
  }

  private _graph: Graphique = new Graphique();

  get graph(): Graphique {
    return this._graph;
  }

  set graph(value: Graphique) {
    this._graph = value;
  }

}
