import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthenticationService } from '../services/Authentication.service';
import { Observable, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> {
    // return of(true);
    return this.authenticationService.isAuthenticated().pipe(
      map((isAuth) => {
        if (!isAuth) {
          this.router.navigate(['/connect']);
          return false;
        }
        return true;
      }),
      switchMap((isAuthenticated) => of(isAuthenticated))
    );
  }
}
