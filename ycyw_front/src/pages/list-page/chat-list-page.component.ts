import { Component, OnInit, OnDestroy, NgZone } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chat, Message, User } from '../../interfaces';
import { RouterLink, Router } from '@angular/router';
import { AuthenticationService } from '../../services/Authentication.service';
import { ChatService } from '../../services/Chat.service';
import { CustomButton } from '../../component/button/button.component';
import { ErrorHandler } from '../../utility/ErrorHandler';
import { Subscription } from 'rxjs';

@Component({
  selector: 'chat-list-page',
  imports: [CommonModule, RouterLink, CustomButton],
  templateUrl: './chat-list-page.component.html',
  styleUrls: ['./chat-list-page.component.css'], // corrigé : styleUrls au pluriel
})
export class ChatListPage implements OnInit, OnDestroy {
  title = 'chat-list-page';
  chats: Chat[] = [];
  updatedChats = new Set<number>();
  messages: Message[] | null = [];
  user: User | null = null;

  chatUpdatesSubscription?: Subscription;

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
        this.chatUpdatesSubscription = this.chatService
          .getChatUpdates(user.id)
          .subscribe({
            next: (chat) => {
              this.ngZone.run(() => {
                const index = this.chats.findIndex((c) => c.id === chat.id);
                if (index !== -1) {
                  this.chats[index] = chat;
                } else {
                  this.chats = [...this.chats, chat].sort(
                    (a, b) =>
                      new Date(b.updatedAt).getTime() -
                      new Date(a.updatedAt).getTime()
                  );
                }
              });
            },
            error: (err) => this.errorHandler.handleError(err),
          });
      },
    });
  }

  ngOnDestroy(): void {
    if (this.chatUpdatesSubscription) {
      this.chatUpdatesSubscription.unsubscribe();
    }
  }

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
