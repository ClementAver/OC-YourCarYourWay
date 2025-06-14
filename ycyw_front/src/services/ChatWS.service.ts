import { Injectable } from '@angular/core';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { Message } from '../interfaces';
import { environment } from '../environment';

@Injectable({ providedIn: 'root' })
export class ChatWSService {
  private client: Client;
  private messageSubject = new Subject<Message>();
  private subscription: StompSubscription | null = null;
  private wsURL = environment.wsURL;

  private connectedPromise: Promise<void>;
  private connectedResolver!: () => void;

  constructor() {
    this.connectedPromise = new Promise((resolve) => {
      this.connectedResolver = resolve;
    });

    this.client = new Client({
      brokerURL: undefined, // fallback to SockJS
      webSocketFactory: () => new SockJS(`${this.wsURL}/chat`),
      reconnectDelay: 5000,
    });

    this.client.onConnect = () => {
      this.connectedResolver();
    };

    this.client.activate(); // initial activation
  }

  async subscribeToChat(chatId: number) {
    if (!this.client.active) {
      // re-activate if previously disconnected
      this.connectedPromise = new Promise((resolve) => {
        this.connectedResolver = resolve;
      });
      this.client.activate();
      await this.connectedPromise;
    }

    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    this.subscription = this.client.subscribe(
      `/topic/chat.${chatId}`,
      (message: IMessage) => {
        this.messageSubject.next(JSON.parse(message.body));
      }
    );
  }

  getMessages(): Observable<Message> {
    return this.messageSubject.asObservable();
  }

  sendMessage(chatId: number, message: any) {
    this.client.publish({
      destination: `/ws/chat.sendMessage.${chatId}`,
      body: JSON.stringify(message),
    });
  }

  disconnect() {
    this.client.deactivate();
  }
}
