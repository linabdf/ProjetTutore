export class Notification {
    private _message: string = '';
    private _lue: boolean = false;
  
    constructor(message: string = '', lue: boolean = false) {
      this._message = message;
      this._lue = lue;
    }
  
    get message(): string {
      return this._message;
    }
  
    set message(value: string) {
      this._message = value;
    }
  
    get lue(): boolean {
      return this._lue;
    }
  
    set lue(value: boolean) {
      this._lue = value;
    }
  }
  