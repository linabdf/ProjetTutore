import {Component, inject} from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import { UserService } from '../signin/user/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  standalone: true,
  styleUrls: ['./login.component.css']
})

export class LoginComponent {
  user = { email: '', password: '' };

  constructor(private userService: UserService, private router: Router) {

  }
  fb = inject(FormBuilder);

  form = this.fb.nonNullable.group({
    email: ['', Validators.required],
    password: ['', Validators.required]
  })

 // Cette méthode permet de construire le payload de l'utilisateur pour la connexion
 toPayload(): any {
  return {
    "email": this.user.email,
    "password": this.user.password
  };
}

onSubmit() {
    if (this.form.valid) {
      this.user.email = this.form.get('email')!.value;
      this.user.password = this.form.get('password')!.value;
      // Affichage dans la console pour déboguer les champs
      console.debug("Email: ", this.user.email);
      console.debug("Password: ", this.user.password);
      this.loginUser();

    }
  }
  loginUser() {
    const userPayload = this.toPayload();

    // Appel au service pour se connecter
    this.userService.loginUser(userPayload).subscribe(
      data => {
        console.log("Connexion réussie :", data);
        this.router.navigateByUrl('/main');


      },
      error => {
        console.error("errur d'email ou de mots de passe merci de verifier vos cordonnées :", error);
      }
    );
  }
}
