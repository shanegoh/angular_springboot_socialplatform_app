import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthenticationService } from '../_services/authentication.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentUser = this.authenticationService;

        if (currentUser.isAuthenticated() && currentUser.hasToken()) {
            // check if route is restricted by role
            if (route.data['roles'] && route.data['roles'].indexOf(currentUser.getRole()) !== -1 ) {
                // authorised so return true
                return true
            }
              // role not authorised so redirect to main
              console.log("Invalid Access")
              this.router.navigate(['']);
              return false
        }

        // not logged in so redirect to login page with the return url
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
    }
}