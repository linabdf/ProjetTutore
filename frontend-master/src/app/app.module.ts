import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { NotificationService } from './notification/notification.service';  
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [],
  imports: [BrowserModule, CommonModule ,HttpClientModule],  // Ajouter HttpClientModule ici
// Initialiser Firebase avec la configuration
 
providers: [NotificationService], 
  bootstrap: []
})
export class AppModule {}
