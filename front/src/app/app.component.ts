import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { Menu } from '../component/menu/menu.component';
import { environment } from '../environment';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Menu, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = environment.title;
  menuIsVisible = true;
  navIsVisible = true;

  ngOnInit() {
    document.documentElement.style.setProperty(
      '--primary-color',
      environment.primaryColor || 'blue'
    );
    document.documentElement.style.setProperty(
      '--secondary-color',
      environment.secondaryColor || 'red'
    );
  }

  constructor(private router: Router) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        (event as NavigationEnd).urlAfterRedirects !== '/connect'
          ? (this.menuIsVisible = true)
          : (this.menuIsVisible = false);
        (event as NavigationEnd).urlAfterRedirects !== '/login' &&
        (event as NavigationEnd).urlAfterRedirects !== '/register'
          ? (this.navIsVisible = true)
          : (this.navIsVisible = false);
      }
    });
  }
}
