import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'back-button',
  standalone: true,
  imports: [ RouterLink],
  templateUrl: './backButton.component.html',
  styleUrls: ['./backButton.component.css'],
})
export class BackButton {
  @Input() route: string = '';
}
