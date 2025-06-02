import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { ErrorHandler } from '../utility/ErrorHandler';
import { AuthenticationService } from './Authentication.service';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Chat } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class ChatService {
  private apiURL = environment.apiURL;

  constructor(
    private httpClient: HttpClient,
    private errorHandler: ErrorHandler,
    private authenticationService: AuthenticationService
  ) {}

  createChat(user: string): Observable<Chat> {
    return this.httpClient
      .post<Chat>(
        `${this.apiURL}/chat`,
        { user: user },
        {
          headers: this.authenticationService.getHeaders(),
        }
      )
      .pipe(catchError((error) => this.errorHandler.handleError(error)));
  }

  getChat(chatId: number): Observable<Chat> {
    return this.httpClient
      .get<Chat>(`${this.apiURL}/chat/${chatId}`, {
        headers: this.authenticationService.getHeaders(),
      })
      .pipe(catchError((error) => this.errorHandler.handleError(error)));
  }

  getChatsByUserId(userId: number): Observable<Chat[]> {
    return this.httpClient
      .get<Chat[]>(`${this.apiURL}/chat/user/${userId}`, {
        headers: this.authenticationService.getHeaders(),
      })
      .pipe(catchError((error) => this.errorHandler.handleError(error)));
  }

  deleteChat(chatId: number): Observable<number> {
    return this.httpClient
      .delete<number>(`${this.apiURL}/chat/${chatId}`, {
        headers: this.authenticationService.getHeaders(),
      })
      .pipe(catchError((error) => this.errorHandler.handleError(error)));
  }

  getChatUpdates(userId: number): EventSource {
    const token = this.authenticationService.getAccessToken();
    return new EventSource(
      `${this.apiURL}/chat/user/${userId}/stream?token=${token}`
    );
  }
}
