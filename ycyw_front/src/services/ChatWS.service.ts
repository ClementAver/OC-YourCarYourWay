import { Injectable } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { Message } from '../interfaces';
import { environment } from '../environment';
import { ErrorHandler } from '../utility/ErrorHandler';

@Injectable({ providedIn: 'root' })
export class ChatWSService {
  private client: Client;
  private messageSubject = new Subject<Message>();
  private subscription: StompSubscription | null = null;
  private wsURL = environment.wsURL;

  private connectedPromise: Promise<void>;
  private connectedResolver!: () => void;

  constructor(private errorHandler: ErrorHandler) {
    this.connectedPromise = new Promise((resolve) => {
      this.connectedResolver = resolve;
    });

    this.client = new Client({
      brokerURL: undefined, // ↓
      webSocketFactory: () => new SockJS(`${this.wsURL}/chat`),
      reconnectDelay: 5000,
    });

    this.setupClientHandlers();

    try {
      this.client.activate(); // Initializes the connection.
    } catch (error) {
      this.errorHandler.handleError(error);
    }
  }

  private setupClientHandlers() {
    this.client.onConnect = () => {
      this.connectedResolver();
    };

    this.client.onStompError = (frame) => {
      const msg = `Erreur STOMP : ${frame.headers['message']}\n${frame.body}`;
      this.errorHandler.handleError(new Error(msg));
    };

    this.client.onWebSocketError = (event) => {
      this.errorHandler.handleError(new Error('Erreur WebSocket : ' + event));
    };
  }

  async subscribeToChat(chatId: number) {
    if (!this.client.active || !this.client.connected) {
      this.connectedPromise = new Promise((resolve) => {
        this.connectedResolver = resolve;
      });

      try {
        this.client.activate();
      } catch (error) {
        this.errorHandler.handleError(error);
      }

      await this.connectedPromise;
    }

    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    try {
      this.subscription = this.client.subscribe(
        `/topic/chat.${chatId}`,
        (message: IMessage) => {
          const parsed = JSON.parse(message.body);
          this.messageSubject.next(parsed);
        }
      );
    } catch (error) {
      this.errorHandler.handleError(error);
    }
  }

  getMessages(): Observable<Message> {
    return this.messageSubject.asObservable();
  }

  sendMessage(chatId: number, message: any) {
    try {
      if (!this.client.connected) {
        throw new Error('Cannot send message: WebSocket is not connected.');
      }

      this.client.publish({
        destination: `/ws/chat.sendMessage.${chatId}`,
        body: JSON.stringify(message),
      });
    } catch (error) {
      this.errorHandler.handleError(error);
    }
  }

  disconnect() {
    try {
      this.client.deactivate();
    } catch (error) {
      this.errorHandler.handleError(error);
    }
  }
}
