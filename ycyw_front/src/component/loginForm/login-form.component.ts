import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CustomButton } from '../button/button.component';
import { CustomInput } from '../input/input.component';
import { AuthenticationService } from '../../services/Authentication.service';

@Component({
  selector: 'login-form',
  standalone: true,
  imports: [ReactiveFormsModule, CustomInput, CustomButton, CommonModule],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
})
export class LoginForm {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  mailRegex = new RegExp('^[\\w\\-.]+@([\\w\\-]+\\.)+[\\w\\-]{2,4}$');

  loginForm = new FormGroup({
    identifier: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(256),
      Validators.pattern(
        '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$'
      ),
    ]),
  });

  get identifier() {
    return this.loginForm.get('identifier');
  }

  get password() {
    return this.loginForm.get('password');
  }

  isMail() {
    const identifier = this.loginForm.value.identifier;
    return (
      identifier && identifier.length > 0 && this.mailRegex.test(identifier)
    );
  }

  handleSubmit() {
    const requestBody = {
      name: this.isMail() ? '' : (this.loginForm.value.identifier as string),
      email: this.isMail() ? (this.loginForm.value.identifier as string) : '',
      password: this.loginForm.value.password as string,
    };

    this.authenticationService.login(requestBody).subscribe({
      next: () => {
        this.router.navigate(['/conversation-list']);
      },
    });
  }
}
