import {Graphique} from './graph';

export class Site {

  private _numS: number = 0;
  private _urlSite: string = ""; // url global du site
  private _urlArticle: string = ""; // url de l'article sur le site
  private _nomSite: string = "";
  private _graph: Graphique = new Graphique(); // graphique des prix de l'article sur ce site

  //setters et getters
  get numS(): number {
    return this._numS;
  }

  set numS(value: number) {
    this._numS = value;
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

  get nomSite(): string {
    return this._nomSite;
  }

  set nomSite(value: string) {
    this._nomSite = value;
  }

  get graph(): Graphique {
    return this._graph;
  }

  set graph(value: Graphique) {
    this._graph = value;
  }

}