  import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
  import { Injectable } from '@angular/core';
  import { Observable } from 'rxjs';
  import { LoginService } from './login.service';

  @Injectable({
    providedIn: 'root'
  })
  export class JwtInterceptorService implements HttpInterceptor{

    constructor(private loginService:LoginService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

      const token = sessionStorage.getItem('token');
      console.log(token)
    // Verificar si el interceptor está inicializado
  console.log("valor",this.loginService.currentUserLoginOn.value)
    // console.log(token)
    
    const valor=!this.loginService.currentUserLoginOn.value;

    if (valor) {
      console.log("El usuario no está autenticado, pasando la solicitud sin token.");
      return next.handle(req);
    }
    
    console.log(token)
      if (token != null && token != '') {
        console.log(`aqui el token es diferente de nulo ${token}`);

        req = req.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }
    
    return next.handle(req);
    }
  }
