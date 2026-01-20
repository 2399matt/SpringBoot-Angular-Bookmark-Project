import {FormControl, ValidationErrors} from "@angular/forms";

export class UrlValidator {

  static isValidUrl(control: FormControl): ValidationErrors | null {
    if(control.value != null) {
      const hasValidTld = /\.(com|academy|org|net|edu|gov|io|co|dev)([\/\?#].*)?$/i.test(control.value);
      if(hasValidTld) {
        return null;
      }
    }
    return {isValidUrl: true};
  }
}
