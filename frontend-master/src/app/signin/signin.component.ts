import { Component } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { Router, RouterModule } from '@angular/router';
import { UserService } from './user/user.service';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [
    FormsModule, RouterModule
  ],
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']  // Utilise styleUrls pour le fichier CSS
})
export class SigninComponent {
  
   user = { username: '', password: '', email: '', firstname: '' };

  constructor(private userService: UserService, private router: Router) {}

  // Cette méthode permet de construire le payload de l'utilisateur
  toPayload(): any {
    return {
     "username": this.user.username,
      "password": this.user.password,
      "email": this.user.email,
      "firstname": this.user.firstname,
     
    };
  }

  onSubmit() {
    // Affichage dans la console pour déboguer les champs
    console.debug("Email: ", this.user.email);
    console.debug("Username: ", this.user.username);
    console.debug("Firstname: ", this.user.firstname);
    console.debug("Password: ", this.user.password);

    this.saveUser();
  }

  saveUser() {
    const userPayload = this.toPayload();

    // Appel au service pour enregistrer l'utilisateur
    this.userService.createUser(userPayload).subscribe(
      data => {
        console.log("Utilisateur créé avec succès :", data);
        this.router.navigateByUrl('');  // Redirection après succès
      },
      error => {
        console.error("Erreur lors de la création de l'utilisateur :", error);
      }
    );
  }
}
