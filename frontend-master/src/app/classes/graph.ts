export class Graphique {
  private _dataPoints: { x: Date, y: number, toolTipContent?: string }[] = []; // Données du graphique

  constructor() {
  }

  get dataPoints(): { x: Date, y: number, toolTipContent?: string }[] {
    return this._dataPoints;
  }

  set dataPoints(value: { x: Date, y: number, toolTipContent?: string }[]) {
    this._dataPoints = value;
  }
}