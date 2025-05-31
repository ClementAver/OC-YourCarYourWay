import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environment';
import { ErrorHandler } from '../utility/ErrorHandler';
import { AuthenticationService } from './Authentication.service';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Message } from '../interfaces';

@Injectable({ providedIn: 'root' })
export class MessageService {
  private apiURL = environment.apiURL;

  constructor(
    private httpClient: HttpClient,
    private errorHandler: ErrorHandler,
    private authenticationService: AuthenticationService
  ) {}

  getMessagesByChatId(chatId: number): Observable<Message[]> {
    return this.httpClient
      .get<Message[]>(`${this.apiURL}/message/chat/${chatId}`, {
        headers: this.authenticationService.getHeaders(),
      })
      .pipe(catchError((error) => this.errorHandler.handleError(error)));
  }

  createMessage(
    content: string,
    chat: number,
    user: number
  ): Observable<Message> {
    return this.httpClient
      .post<Message>(
        `${this.apiURL}/message`,
        { content, chat, user },
        {
          headers: this.authenticationService.getHeaders(),
        }
      )
      .pipe(catchError((error) => this.errorHandler.handleError(error)));
  }
}
