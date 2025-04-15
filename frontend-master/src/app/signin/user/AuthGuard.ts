import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {

    // Vérifier si le token est présent dans le localStorage (ou sessionStorage)
    const isAuthenticated = localStorage.getItem('token') !== null;

    if (isAuthenticated) {
      // Si l'utilisateur est authentifié, permettre l'accès
      return true;
    } else {
      // Si l'utilisateur n'est pas authentifié, rediriger vers la page de connexion
      this.router.navigateByUrl('/login');
      return false;
    }
  }
}