import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chat, Message, User } from '../../interfaces';
import { RouterLink, Router } from '@angular/router';
import { AuthenticationService } from '../../services/Authentication.service';
import { ChatService } from '../../services/Chat.service';
import { CustomButton } from '../../component/button/button.component';
import { NgZone } from '@angular/core';
import { ErrorHandler } from '../../utility/ErrorHandler';

@Component({
  selector: 'chat-list-page',
  imports: [CommonModule, RouterLink, CustomButton],
  templateUrl: './chat-list-page.component.html',
  styleUrl: './chat-list-page.component.css',
})
export class ChatListPage implements OnInit {
  title = 'chat-list-page';
  chats: Chat[] = [];
  updatedChats = new Set<number>();
  messages: Message[] | null = [];
  user: User | null = null;
  eventSource?: EventSource;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private chatService: ChatService,
    private ngZone: NgZone,
    private errorHandler: ErrorHandler
  ) {}

  ngOnInit(): void {
    // Fetches the user.
    this.authenticationService.me().subscribe({
      next: (user) => {
        this.user = user;
        // Fetches the chats for the user.
        this.chatService.getChatsByUserId(user.id).subscribe({
          next: (chats: Chat[]) => {
            this.chats = chats.sort((a, b) => {
              return (
                new Date(b.updatedAt).getTime() -
                new Date(a.updatedAt).getTime()
              );
            });
            if (user.role === 'CUSTOMER' && chats && chats.length === 1) {
              this.goToChatPage(chats[0].id);
            }
          },
        });

        // Initializes an eventSource for the user chats updates.
        let eS = this.chatService.getChatUpdates(user.id || 0);

        this.eventSource = eS;

        eS.onmessage = (event) => {
          this.ngZone.run(() => {
            const chat: Chat = JSON.parse(event.data);
            const chatIndex = this.chats.findIndex((c) => c.id === chat.id);
            if (chatIndex !== -1) {
              this.chats[chatIndex] = chat;
            } else {
              const tmpChats = [...this.chats];
              tmpChats.push(chat);
              tmpChats.sort((a, b) => {
                return (
                  new Date(b.updatedAt).getTime() -
                  new Date(a.updatedAt).getTime()
                );
              });
              this.chats = tmpChats;
            }
          });
        };

        // eS.onerror = (error) => {
        //   this.eventSource?.close();
        //   this.errorHandler.handleError(error);
        // };
      },
    });
  }

  // ngOnDestroy(): void {
  //   // Closes the eventSource when the component is destroyed.
  //   if (this.eventSource) {
  //     this.eventSource.close();
  //   }
  // }

  goToChatPage(id: number) {
    this.router.navigate(['/conversation', id]);
  }

  deleteChat(chatId: number) {
    this.chatService.deleteChat(chatId).subscribe({
      next: () => {
        this.chats = this.chats?.filter((chat) => chat.id !== chatId);
      },
    });
  }

  createChat(userId: number) {
    this.chatService.createChat(userId.toString()).subscribe({
      next: (chat) => {
        this.chats.push(chat);
        this.goToChatPage(chat.id);
      },
    });
  }

  handleCreateChat(userId: number): () => void {
    return () => this.createChat(userId);
  }

  handleDeleteChat(chatId: number): () => void {
    return () => this.deleteChat(chatId);
  }
}
