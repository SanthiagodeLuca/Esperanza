import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { User } from 'src/app/modelos/user';
import { environment } from 'src/enviroments/enviroments';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  getUser(id:number):Observable<User>{
  
    
    return this.http.get<User>(environment.API_BACKEND_URL_USER+id).pipe(

      catchError(this.handleError)
    );

  }
  private handleError(error:HttpErrorResponse){
    if(error.status==0){
      console.log('se ha producido un error',error.error);
    }else{

      console.error('el backend retorno el codigo de estado',error.status,error.error)
    }
    return throwError(()=>new Error('Algo falllo intente de nuvevo'));
  }

/* Aun no tengo implementado todo lo demas 
  updateUser(userRequest:User):Observable<any>
  {
    return this.http.put(environment.API_BACKEND_URL_USER+"user",userRequest).pipe(

      catchError(this.handleError)
    );
  }*/
}
