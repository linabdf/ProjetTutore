export class Graphique {
  private _dataPoints: { x: Date, y: number, toolTipContent?: string }[] = []; // Données du graphique
  private _filteredDps : { x: Date, y: number, toolTipContent?: string }[] = [];
  private _options : any = {};

  constructor() {
  }

  get dataPoints(): { x: Date, y: number, toolTipContent?: string }[] {
    return this._dataPoints;
  }

  set dataPoints(value: { x: Date, y: number, toolTipContent?: string }[]) {
    this._dataPoints = value;
  }

  get options(): any {
    return this._options;
  }

  set options(value: any) {
    this._options = value;
  }

  get filteredDps(): { x: Date; y: number; toolTipContent?: string }[] {
    return this._filteredDps;
  }

  set filteredDps(value: { x: Date; y: number; toolTipContent?: string }[]) {
    this._filteredDps = value;
  }
}