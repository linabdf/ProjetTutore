import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {UserService} from '../signin/user/user.service';
@Injectable({
  providedIn: 'root'
})

export class GuardLogged implements CanActivate {

  constructor(private userService: UserService,
              private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.userService.isAuthenticated()) {
      this.router.navigateByUrl('/main');
      return true;
    } else {
      return false;
    }
  }

}
