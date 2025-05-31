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

  createCommentForm = new FormGroup({
    content: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
  });

  get content() {
    return this.createCommentForm.get('content');
  }

  constructor(
    private route: ActivatedRoute,
    private chatService: ChatService,
    private authenticationService: AuthenticationService,
    private messageService: MessageService,
    private userService: UserService
  ) {
    this.route.paramMap.subscribe((params) => {
      this.id = Number(params.get('id')) || 0;
    });
  }

  ngOnInit(): void {
    this.authenticationService.me().subscribe({
      next: (user) => {
        this.user = user;
        this.getChats();
      },
    });
  }

  getChats(): void {
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
    });
  }

  handleSubmit(): void {
    if (this.createCommentForm.invalid || !this.chat) {
      return;
    }
    const content = this.createCommentForm.value.content?.trim();
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
          this.createCommentForm.reset();
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
