import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ErrorHandler {
  handleError(error: any, skipAlert: boolean = false): Observable<never> {
    let errorMessage = 'An unknown error occurred';
    if (error.error && error.error.errors) {
      errorMessage = error.error.errors.join('\n');
    } else if (error.error && error.error.error) {
      errorMessage = error.error.error;
    } else if (error.error) {
      errorMessage = error.error;
    } else {
      errorMessage = error.message;
    }

    console.error(error);
    if (!skipAlert) alert(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
