import { Component, OnInit } from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {Router} from '@angular/router';
import {UserService} from '../signin/user/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-main',
  standalone: true,  
  imports: [
  CommonModule, FormsModule
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent implements OnInit {
  constructor(private router: Router,private UserService:UserService) {}
  articles: any[] = [];
 
  ajouterArticle() {
    this.router.navigateByUrl('/AjouterArticle');
    
    return "ajout test";
  }
  deconnexion() {
    this.UserService.logout
    this.router.navigateByUrl('');
    return "ajout test";
  }
  ngOnInit() {
    console.log('ngOnInit appelé');
     
    if (this.UserService.isAuthenticated()) {
    this.UserService.getMyArticles().subscribe(
      (data) => {
        this.articles = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des articles :', error);
      }
    );
  }
}
  
}
