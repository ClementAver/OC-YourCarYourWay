import { Component } from '@angular/core';
import { LoginForm } from '../../component/loginForm/login-form.component';
import { BackButton } from '../../widgets/backButton/backButton.component';

@Component({
  selector: 'login',
  standalone: true,
  imports: [LoginForm, BackButton],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class Login {}
