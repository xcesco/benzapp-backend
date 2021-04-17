import { Component, forwardRef, OnInit, Optional, Self } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, NgControl } from '@angular/forms';

@Component({
  selector: 'jhi-numeric-keys',
  templateUrl: './numeric-keys.component.html',
  styleUrls: ['./numeric-keys.component.scss'],
  // providers: [
  //   {
  //     provide: NG_VALUE_ACCESSOR,
  //     useExisting: forwardRef(() => NumericKeysComponent),
  //     multi: false
  //   }
  // ]
})
export class NumericKeysComponent implements OnInit, ControlValueAccessor {
  numberpad = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '<'];

  constructor(@Optional() @Self() public ngControl: NgControl) {
    // Replace the provider from above with this.

    // Setting the value accessor directly (instead of using
    // the providers) to avoid running into a circular import.
    this.ngControl.valueAccessor = this;
  }

  ngOnInit(): void {
    console.error('ngOnInit');
  }

  registerOnChange(fn: any): void {
    console.error('registerOnChange', fn);
  }

  registerOnTouched(fn: any): void {
    console.error('registerOnTouched', fn);
  }

  writeValue(obj: any): void {
    console.error('writeValue');
  }

  updateValue(char: string): void {
    console.error('updateValue', char);

    const currentValue = String(this.ngControl.control?.value ?? '');
    if (char === '<') {
      this.ngControl.control?.setValue(currentValue.slice(0, -1));
    } else {
      if (char === '.' && currentValue.indexOf('.') !== -1) {
        return;
      }
      const newValue = currentValue + char;
      console.error(char, newValue);
      this.ngControl.control?.setValue(newValue);
    }
  }
}
