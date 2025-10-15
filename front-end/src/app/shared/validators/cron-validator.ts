import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { isValidCron } from 'cron-validator';

/**
 * Validador personalizado para expressões cron
 * Suporta formatos de 5 campos (padrão Unix) e 6 campos (Quartz)
 */
export function cronExpressionValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null; // Deixe o validador required lidar com campos vazios
    }

    // Verifica se é uma expressão cron válida (formato Quartz com segundos)
    const isValid = isValidCron(control.value, { seconds: true });
    
    return isValid ? null : { invalidCronExpression: true };
  };
}