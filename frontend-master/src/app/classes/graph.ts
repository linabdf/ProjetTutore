export class Graphique {
  private _nom: string = "";
  private _type: string = "line";
  private _dataPoints: { x: Date, y: number }[] = []; // Données du graphique

  constructor() {
  }


  get type(): string {
    return this._type;
  }

  set type(value: string) {
    this._type = value;
  }

  get dataPoints(): any[] {
    return this._dataPoints;
  }

  set dataPoints(value: any[]) {
    this._dataPoints = value;
  }

  get nom(): string {
    return this._nom;
  }

  set nom(value: string) {
    this._nom = value;
  }
}
