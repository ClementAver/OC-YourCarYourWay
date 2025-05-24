import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

import { User, Message, Chat } from '../../interfaces';
import {
  getChat,
  getMessages,
  getUser,
  postMessage,
} from '../../services/chat.service';
import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { AnimatedSign } from '../../component/animatedSign/animated-sign.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'chat-page',
  imports: [CommonModule, ReactiveFormsModule, AnimatedSign],
  templateUrl: './chat-page.component.html',
})
export class ChatPage {
  createCommentForm = new FormGroup({
    content: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
  });

  get content() {
    return this.createCommentForm.get('content');
  }

  title = 'chat-page';

  id: number = 0;

  constructor(private route: ActivatedRoute) {
    this.route.paramMap.subscribe((params) => {
      this.id = Number(params.get('id')) || 0;
      this.loadChatData();
    });
  }

  chat: Chat | null = null;
  messages: Message[] | null = [];
  employee: User | null = null;
  customer: User | null = null;

  loadChatData() {
    this.chat = getChat(this.id);
    this.messages = this.chat ? getMessages(this.chat.id) : [];
    this.employee = this.chat ? getUser(this.chat.employee_id) : null;
    this.customer = this.chat ? getUser(this.chat.customer_id) : null;
  }

  handleSubmit(): void {
    postMessage(
      this.chat?.id as number,
      this.customer?.id as number,
      this.createCommentForm.value.content as string,
      this.messages as Message[]
    );
  }

  isFromEmployee(message: Message): boolean {
    return message.author_id === this.employee?.id;
  }
}
