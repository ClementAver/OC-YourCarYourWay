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

  constructor() {
    this.client = new Client({
      brokerURL: undefined, //  ↓ 
      webSocketFactory: () => new SockJS(`${this.wsURL}/chat`),
      reconnectDelay: 5000,
    });
    this.client.onConnect = () => {};
    // Starts the connection
    this.client.activate();
  }

  subscribeToChat(chatId: number) {
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
