

export class User {

    private _username: string = "";
   
    private _password : string = "";
    private _firstname : string = "";
  
    private _email : string = "";
    constructor() {
    }
  
    get username() : string{
      return this._username;
    }
    set username(username: string){
      this._username =username;
    }
  
    get password(): string {
      return this._password;
    }
  
    set password(value: string) {
      this._password = value;
    }
    get firstname(): string {
      return this._firstname;
    }
  
    set firstname(value: string) {
      this._firstname = value;
    }
  
    get email(): string {
      return this._email;
    }
  
    set email(value: string) {
      this._email = value;
    }
  }
  
  
  