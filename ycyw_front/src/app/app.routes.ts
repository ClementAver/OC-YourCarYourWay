import { Routes } from '@angular/router';
import { ChatPage } from '../pages/chat-page/chat-page.component';
import { ChatListPage } from '../pages/list-page/chat-list-page.component';
import { Connect } from '../pages/connect-page/connect.component';
import { Login } from '../pages/login-page/login.component';
import { Register } from '../pages/register-page/register.component';
import { ConnectGuard } from '../guards/ConnectGuard';
import { AuthGuard } from '../guards/AuthGuard';
import { NotFound } from '../pages/not-found-page/not-found.component';
import { Profile } from '../pages/profile-page/profile.component';

export const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'connect',
        component: Connect,
        canActivate: [ConnectGuard],
      },
      { path: 'login', component: Login },
      { path: 'register', component: Register },
      { path: 'profile', component: Profile, canActivate: [AuthGuard] },
      {
        path: 'conversation-list',
        component: ChatListPage,
        canActivate: [AuthGuard],
      },
      {
        path: 'conversation/:id',
        component: ChatPage,
        canActivate: [AuthGuard],
      },
      { path: '', redirectTo: '/connect', pathMatch: 'full' },
      { path: '**', component: NotFound },
    ],
  },
];
