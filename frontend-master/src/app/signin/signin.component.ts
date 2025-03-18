import {Component, inject} from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import { Router, RouterModule } from '@angular/router';
import { UserService } from './user/user.service';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [
    FormsModule, RouterModule, ReactiveFormsModule
  ],
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']  // Utilise styleUrls pour le fichier CSS
})

export class SigninComponent {

  user = { username: '', password: '', email: '', firstname: '' };

  constructor(private userService: UserService, private router: Router) {}

  fb = inject(FormBuilder);
  form = this.fb.nonNullable.group({
    username: ['', Validators.required],
    firstname: ['', Validators.required],
    email: ['', Validators.required],
    password: ['', Validators.required],
    mdpconf: ['', Validators.required],
  })

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
    if (this.form.valid) {
      if(this.form.get('password')?.value == this.form.get('mdpconf')?.value) {

        this.user.username = this.form.get('username')!.value;
        this.user.password = this.form.get('password')!.value;
        this.user.email = this.form.get('email')!.value;
        this.user.firstname = this.form.get('firstname')!.value;

        // Affichage dans la console pour déboguer les champs
        console.debug("Email: ", this.user.email);
        console.debug("Username: ", this.user.username);
        console.debug("Firstname: ", this.user.firstname);
        console.debug("Password: ", this.user.password);

        this.saveUser();
      } else {
        window.alert("Les mots de passe de correspondent pas");
      }
    } else {
      window.alert("Le formulaire est invalide");
    }


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
