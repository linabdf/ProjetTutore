import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClient } from '@angular/common/http';  // Assure-toi d'importer HttpClientModule
import { CommonModule } from '@angular/common/';

@NgModule({
  declarations: [],
  imports: [BrowserModule, CommonModule],  // Ajouter HttpClientModule ici
  providers: [],
  bootstrap: []
})

export class AppModule {}
