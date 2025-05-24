import { Component, Input } from '@angular/core';

@Component({
  selector: 'custom-button',
  standalone: true,
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css'],
})
export class CustomButton {
  @Input() type: 'button' | 'submit' | 'reset' = 'button';
  @Input() text: string = '';
  @Input() classes: {
    outlined?: boolean;
    disabled?: boolean;
    logout?: boolean;
  } = {
    outlined: false,
    disabled: false,
    logout: false,
  };
  @Input() action: Function = () => {};

  switchEnability() {
    this.classes.disabled = !this.classes.disabled;
  }
}
