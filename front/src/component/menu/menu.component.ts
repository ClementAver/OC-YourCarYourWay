import { Component, Input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'menu',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
})
export class Menu {
  @Input() navIsVisible: boolean = true;

  classes = {
    hidden: true,
    navIsVisible: this.navIsVisible,
  };

  toggleDisplay() {
    if (window.innerWidth < 768) {
      this.classes = { ...this.classes, hidden: !this.classes.hidden };
    }
  }

  ngOnChanges() {
    this.classes.navIsVisible = this.navIsVisible;
  }
}
