import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';

import { User, Message, Chat } from '../../interfaces';
import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AnimatedSign } from '../../component/animatedSign/animated-sign.component';
import { ActivatedRoute } from '@angular/router';
import { ChatService } from '../../services/Chat.service';
import { AuthenticationService } from '../../services/Authentication.service';
import { MessageService } from '../../services/Message.service';
import { UserService } from '../../services/User.service';
import { Router } from '@angular/router';
@Component({
  selector: 'chat-page',
  imports: [CommonModule, ReactiveFormsModule, AnimatedSign],
  templateUrl: './chat-page.component.html',
})
export class ChatPage implements OnInit {
  chat: Chat | null = null;
  messages: Message[] | null = [];
  user: User | null = null;
  customer: User | null = null;
  title = 'chat-page';
  id: number = 0;

  createMessageForm = new FormGroup({
    content: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
  });

  get content() {
    return this.createMessageForm.get('content');
  }

  constructor(
    private route: ActivatedRoute,
    private chatService: ChatService,
    private authenticationService: AuthenticationService,
    private messageService: MessageService,
    private userService: UserService,
    private router: Router
  ) {
    this.route.paramMap.subscribe((params) => {
      this.id = Number(params.get('id')) || 0;
    });
  }

  ngOnInit(): void {
    this.authenticationService.me().subscribe({
      next: (user) => {
        this.user = user;
        this.getChat();
      },
    });
  }

  getChat(): void {
    this.chatService.getChat(this.id).subscribe({
      next: (chat) => {
        this.chat = chat;
        if (chat && chat.customer) {
          this.userService.getUser(chat.customer).subscribe({
            next: (customer) => {
              this.customer = customer;
            },
          });
        }
        if (chat && chat.id) {
          this.messageService.getMessagesByChatId(chat.id).subscribe({
            next: (messages) => {
              this.messages = messages;
            },
          });
        }
      },
      error: () => {
        this.router.navigate(['/conversation-list']);
      },
    });
  }

  handleSubmit(): void {
    if (this.createMessageForm.invalid || !this.chat) {
      return;
    }
    const content = this.createMessageForm.value.content?.trim();
    if (!content) {
      return;
    }
    this.messageService
      .createMessage(content, this.chat.id, this.user?.id || 0)
      .subscribe({
        next: (message) => {
          if (this.messages) {
            this.messages.push(message);
          }
          this.createMessageForm.reset();
        },
        error: () => {
          this.router.navigate(['/conversation-list']);
        },
      });
  }

  isFromUser(message: Message): boolean {
    return message.user === this.user?.id;
  }

  formatDate(date: Date): string {
    const options: Intl.DateTimeFormatOptions = {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    };
    return new Intl.DateTimeFormat('fr-FR', options).format(new Date(date));
  }
}
