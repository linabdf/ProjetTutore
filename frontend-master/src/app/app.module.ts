import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';  // Assure-toi d'importer HttpClientModule
import { CommonModule } from '@angular/common';
import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
@NgModule({
  declarations: [],
  imports: [BrowserModule, CommonModule ,HttpClientModule],  // Ajouter HttpClientModule ici
  providers: [],
  bootstrap: []
})
export class AppModule {}
