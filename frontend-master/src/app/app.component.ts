import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import { SigninComponent } from './signin/signin.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'ComparateurBonsPlans';
}
