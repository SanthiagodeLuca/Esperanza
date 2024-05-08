import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError,BehaviorSubject ,tap, map, finalize} from 'rxjs';
import { loginRequest } from 'src/app/modelos/loginRequest';
import { User } from 'src/app/modelos/user';
import { environment } from 'src/enviroments/enviroments';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  //valor cuando inicia
currentUserLoginOn:BehaviorSubject<boolean>= new BehaviorSubject<boolean>(false);
//valor de la sesion session storage
currentUserData:BehaviorSubject<String>=new BehaviorSubject<String>("");
//llamar al metodo next para decirle que ya esta a informacion de los componentes que se subcribieron


private tokenStorageKey = 'userToken'; 

//programacion asincronica
  constructor(private http:HttpClient) { 
    this.currentUserLoginOn=new BehaviorSubject<boolean>(sessionStorage.getItem("token")!=null);
    this.currentUserData=new BehaviorSubject<String>(sessionStorage.getItem("token") || "");
     
  }

  





  //comunicar con la apiRest
  //coge las credenciales del usuario como argumento y devuelve un observable
 /* login(credentials:loginRequest):Observable<any>{
    console.log('Iniciando solicitud de inicio de sesión...');
    console.log('Credenciales:', credentials);
    return this.http.post<any>("http://localhost:8085/auth/login",credentials).pipe( 
        
    tap((userData)=>{
        console.log('hola soy tap');
        console.log('Respuesta del servidor:', userData);
        sessionStorage.setItem("token",userData.token);
      this.currentUserData.next(userData.token);
      this.currentUserLoginOn.next(true);
      console.log('Inicio de sesión exitoso.');
      this.currentUserData.subscribe(value => console.log('Valor actual del currentUserData:', value));

      }), 
      
    //transforma
    map((userData)=>userData.token),
      catchError(this.handleError)
    );

  }
  */
  static readonly DEFAULT_CREDENTIALS = {
    "username":"juan@gmail",
    "password":"juan"


}
 obtener(){
  return this.http.get<any>("http://localhost:8085/api/asistencias")
 }
 solicitud(credentials: loginRequest): Observable<any> {
  //  console.log(credentials)
  return this.http.post<any>("http://localhost:8085/auth/login", credentials).pipe(

  tap((userData)=>{
    //console.log('Respuesta del servidor:', userData);
    sessionStorage.setItem("token",userData.token);
   // const dato = sessionStorage.getItem("token");
    //console.log(dato);
    this.currentUserData.next(userData.token);
    this.currentUserLoginOn.next(true);
  }

  ));

  
}


logOut():void{

  sessionStorage.removeItem("token");
  //avisar a todos que ya no tienen acceso lo que estan subcritos
  this.currentUserLoginOn.next(false);

}

private handleError(error: HttpErrorResponse) {
  if (error.error instanceof ErrorEvent) {
    // Error del lado del cliente, como una red inalcanzable
    console.error('Error del lado del cliente:', error.error.message);
  } else if (error.status) {
    // El servidor retornó un código de estado de error, pero no se pudo obtener el mensaje
    console.error(`Código de estado ${error.status}, pero no se pudo obtener el mensaje de error.`);
  } else {
    // Error desconocido, sin código de estado ni mensaje
    console.error('Error desconocido:', error);
  }
  // Retorna un observable con un mensaje de error genérico
  return throwError('Algo salió mal; por favor, inténtalo de nuevo más tarde.');
}
  get userData():Observable<String>{

    return this.currentUserData.asObservable();
  }

  get userloginOn():Observable<boolean>{
    return this.currentUserLoginOn.asObservable();
  }

  get userToken(): Observable<String> {
    return this.currentUserData.asObservable();
  }

  updateToken(token: string): void {
    sessionStorage.setItem(this.tokenStorageKey, token);
  }

  getToken(): string | null {
    return sessionStorage.getItem(this.tokenStorageKey);
  }
}
