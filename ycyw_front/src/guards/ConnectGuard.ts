import { Injectable } from '@angular/core';
import { AuthenticationService } from '../services/Authentication.service';
import { Router, CanActivate } from '@angular/router';
import { Observable, of } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class ConnectGuard implements CanActivate {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean> {
    const skipAlert = true;
    return this.authenticationService.isAuthenticated(skipAlert).pipe(
      tap((isAuth) => {
        if (isAuth) {
          this.router.navigate(['/conversation-list']);
        }
      }),
      switchMap((isAuth) => of(!isAuth))
    );
  }
}
