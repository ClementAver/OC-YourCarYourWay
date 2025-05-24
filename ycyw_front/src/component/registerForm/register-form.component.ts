import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
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
  selector: 'register-form',
  standalone: true,
  imports: [ReactiveFormsModule, CustomInput, CustomButton, CommonModule],
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css'],
})
export class RegisterForm {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  mailRegex = new RegExp('^[\\w\\-.]+@([\\w\\-]+\\.)+[\\w\\-]{2,4}$');

  registerForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(this.mailRegex),
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

  get name() {
    return this.registerForm.get('name');
  }

  get email() {
    return this.registerForm.get('email');
  }

  get password() {
    return this.registerForm.get('password');
  }

  handleSubmit() {
    const requestBody = {
      name: this.registerForm.value.name as string,
      email: this.registerForm.value.email as string,
      password: this.registerForm.value.password as string,
    };

    this.authenticationService.register(requestBody).subscribe({
      next: () => {
        this.router.navigate(['/connect']);
      },
    });
  }
}
