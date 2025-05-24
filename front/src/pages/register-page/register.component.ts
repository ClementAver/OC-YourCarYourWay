import { Component } from '@angular/core';
import { RegisterForm } from '../../component/registerForm/register-form.component';
import { BackButton } from '../../widgets/backButton/backButton.component';

@Component({
  selector: 'register',
  standalone: true,
  imports: [RegisterForm, BackButton],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class Register {}
