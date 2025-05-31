import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UpdateUserFormComponent } from '../../component/update-user-form/update-user-form.component';
import { CustomButton } from '../../component/button/button.component';
import { AuthenticationService } from '../../services/Authentication.service';
import { UserService } from '../../services/User.service';
import { User } from '../../interfaces';

@Component({
  selector: 'profile',
  standalone: true,
  imports: [UpdateUserFormComponent, CustomButton, CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class Profile {
  user: User;
  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.user = {
      id: -1,
      email: '',
      name: '',
      role: 'CUSTOMER',
    };
  }

  ngOnInit(): void {
    this.authenticationService.me().subscribe({
      next: (user) => {
        this.user = user;
      },
    });
  }

  handleLogout() {
    this.authenticationService.logout();
  }
}
