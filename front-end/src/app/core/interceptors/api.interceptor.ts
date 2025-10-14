import { HttpInterceptorFn } from '@angular/common/http';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  // Add any common headers or authentication tokens here if needed
  return next(req);
};