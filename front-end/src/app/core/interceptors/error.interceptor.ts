import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {
  const snackBar = inject(MatSnackBar);
  
  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocorreu um erro desconhecido';
      
      if (error.error instanceof ErrorEvent) {
        // Client-side error
        errorMessage = `Erro: ${error.error.message}`;
      } else {
        // Server-side error
        if (error.status === 0) {
          errorMessage = 'Não foi possível conectar ao servidor. Verifique sua conexão.';
        } else if (error.status === 404) {
          errorMessage = 'Recurso não encontrado.';
        } else if (error.status === 400) {
          errorMessage = 'Dados inválidos. Verifique os campos e tente novamente.';
        } else if (error.status === 401) {
          errorMessage = 'Não autorizado. Faça login novamente.';
        } else if (error.status === 403) {
          errorMessage = 'Acesso negado.';
        } else if (error.status === 500) {
          errorMessage = 'Erro no servidor. Tente novamente mais tarde.';
        }
        
        // Use error message from server if available
        if (error.error && typeof error.error === 'object') {
          if (error.error.message) {
            errorMessage = error.error.message;
          } else if (error.error.error) {
            errorMessage = error.error.error;
          }
        }
      }
      
      snackBar.open(errorMessage, 'Fechar', {
        duration: 5000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
        panelClass: ['error-snackbar']
      });
      
      return throwError(() => error);
    })
  );
};