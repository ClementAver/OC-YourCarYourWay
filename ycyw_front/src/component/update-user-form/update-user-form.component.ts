import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { CustomInput } from '../input/input.component';
import { CustomButton } from '../button/button.component';
import { ProfileService } from '../../services/Profile.service';
import { AuthenticationService } from '../../services/Authentication.service';
import { User as MeResponse } from '../../interfaces';

@Component({
  selector: 'update-user-form',
  standalone: true,
  imports: [ReactiveFormsModule, CustomInput, CustomButton, CommonModule],
  templateUrl: './update-user-form.component.html',
  styleUrl: './update-user-form.component.scss',
})
export class UpdateUserFormComponent {
  constructor(
    private authenticationService: AuthenticationService,
    private profileService: ProfileService
  ) {}

  private me: MeResponse | undefined = undefined;
  ngOnInit() {
    this.authenticationService.me().subscribe({
      next: (response) => {
        this.me = response;
        this.updateUserForm.get('name')?.setValue(response.name);
        this.updateUserForm.get('email')?.setValue(response.email);
      },
    });
  }

  mailRegex = new RegExp('^[\\w\\-.]+@([\\w\\-]+\\.)+[\\w\\-]{2,4}$');

  updateUserForm = new FormGroup({
    name: new FormControl('', [
      Validators.required,
      Validators.maxLength(256),
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(this.mailRegex),
    ]),
    password: new FormControl('', [
      Validators.minLength(8),
      Validators.maxLength(256),
      Validators.pattern(
        '^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$'
      ),
    ]),
  });

  get id() {
    return this.me?.id;
  }

  get name() {
    return this.updateUserForm.get('name');
  }

  get email() {
    return this.updateUserForm.get('email');
  }

  get password() {
    return this.updateUserForm.get('password');
  }

  handleSubmit() {
    const requestBody = {
      name: this.updateUserForm.value.name as string,
      email: this.updateUserForm.value.email as string,
      password: this.updateUserForm.value.password as string,
    };

    this.profileService.updateUser(this.id as number, requestBody).subscribe({
      next: () => {
        this.authenticationService.logout();
      },
    });
  }
}
