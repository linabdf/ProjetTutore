import {Component, inject, OnInit} from '@angular/core';
import {User} from '../signin/user/user';
import {UserService} from '../signin/user/user.service';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-profil',
  imports: [
    FormsModule
  ],
  templateUrl: './profil.component.html',
  standalone: true,
  styleUrl: './profil.component.css'
})

export class ProfilComponent implements OnInit {
  currentUser: User = new User();
  userService = inject(UserService);
  router = inject(Router);

  ngOnInit() { //changer par getUserByID avec l'id de l'utilisateur actuel (ou alors, tu filtres après)
    this.userService.getUser().subscribe(user => {
      this.currentUser = user;
    });

  }

  deconnexion() {
    this.userService.logout;
    this.router.navigateByUrl('');
    return "ajout test";
  }
}
