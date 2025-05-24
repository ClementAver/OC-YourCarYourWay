import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { ErrorHandler } from '../utility/ErrorHandler';
import { AuthenticationService } from './Authentication.service';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { User as UserResponse } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiURL = environment.apiURL;

  constructor(
    private httpClient: HttpClient,
    private errorHandler: ErrorHandler,
    private authenticationService: AuthenticationService
  ) {}

  getUser(id: number): Observable<UserResponse> {
    return this.httpClient
      .get<UserResponse>(`${this.apiURL}/user/${id}`, {
        headers: this.authenticationService.getHeaders(),
      })
      .pipe(catchError((error) => this.errorHandler.handleError(error)));
  }
}
