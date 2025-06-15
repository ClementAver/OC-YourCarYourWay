import { CommonModule } from '@angular/common';
import {
  Component,
  OnInit,
  AfterViewChecked,
  ElementRef,
  ViewChild,
  OnDestroy,
} from '@angular/core';

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
import { ChatWSService } from '../../services/ChatWS.service';
import { ErrorHandler } from '../../utility/ErrorHandler';

@Component({
  selector: 'chat-page',
  imports: [CommonModule, ReactiveFormsModule, AnimatedSign],
  templateUrl: './chat-page.component.html',
  styleUrls: ['./chat-page.component.css'],
})
export class ChatPage implements OnInit, AfterViewChecked, OnDestroy {
  chat: Chat | null = null;
  messages: Message[] | null = [];
  user: User | null = null;
  customer: User | null = null;
  title = 'chat-page';
  id: number = 0;
  scroll: boolean = true;

  @ViewChild('messagesList')
  private messagesList!: ElementRef<HTMLUListElement>;

  ngAfterViewChecked() {
    if (this.scroll) {
      this.scrollAtChange();
      this.scroll = false;
    }
  }

  scrollAtChange(): void {
    this.messagesList.nativeElement.scrollTo({
      top: this.messagesList.nativeElement.scrollHeight,
      behavior: 'smooth',
    });
  }

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
    private router: Router,
    private chatWSService: ChatWSService,
    private errorHandler: ErrorHandler
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

  ngOnDestroy(): void {
    this.chatWSService.disconnect();
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
              this.scroll = true;
              // Souscription WebSocket ici
              this.chatWSService.subscribeToChat(chat.id);
              this.chatWSService.getMessages().subscribe((message) => {
                if (this.messages) {
                  this.messages.push(message);
                  this.scroll = true;
                }
              });
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

    let token;
    try {
      token = this.authenticationService.getAccessToken();
      if (!token) {
        throw new Error('Token manquant.');
      }
    } catch (error) {
      this.errorHandler.handleError(error);
      this.router.navigate(['/connect']);
    }

    // Envoi via WebSocket
    this.chatWSService.sendMessage(this.chat.id, {
      content,
      chat: this.chat.id,
      user: this.user?.id || 0,
      token: token,
    });
    this.createMessageForm.reset();
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
