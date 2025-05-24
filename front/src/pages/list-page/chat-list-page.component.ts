import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { chats } from '../../mock';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'chat-list-page',
  imports: [CommonModule, RouterLink],
  templateUrl: './chat-list-page.component.html',
  styleUrl: './chat-list-page.component.css',
})
export class ChatListPage {
  chats = chats;
}
