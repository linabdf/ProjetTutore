import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import { UserService } from '../signin/user/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  user = { email: '', password: '' }; 
  constructor(private userService: UserService, private router: Router) {}

 // Cette méthode permet de construire le payload de l'utilisateur pour la connexion
 toPayload(): any {
  return {
    "email": this.user.email,
    "password": this.user.password
  };
}

onSubmit() {
  // Affichage dans la console pour déboguer les champs
  console.debug("Email: ", this.user.email);
  console.debug("Password: ", this.user.password);
  this.loginUser();
    
  }
  loginUser() {
    const userPayload = this.toPayload();
  
    // Appel au service pour se connecter
    this.userService.loginUser(userPayload).subscribe(
      data => {
        console.log("Connexion réussie :", data);
  
        
        const token = data.token; 
        localStorage.setItem('auth_token', token);  
  
        // Rediriger vers la page principale
        this.router.navigateByUrl('/main');
      },
      error => {
        console.error("Erreur d'email ou de mot de passe, merci de vérifier vos coordonnées :", error);
      }
    );
  }
}
