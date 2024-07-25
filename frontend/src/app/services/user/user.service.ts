import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, tap, throwError } from 'rxjs';
import { User } from 'src/app/modelos/user';
import { environment } from 'src/enviroments/enviroments';

@Injectable({
  providedIn: 'root'
})
export class UserService {


  private usuarios: User[] = [];
  private apiUrl = 'http://localhost:8085/api/user';

  private usuariosSubject: BehaviorSubject<User[]> = new BehaviorSubject(this.usuarios);
  constructor(private http:HttpClient) {
    this.cargarUsuarios();
   }
  


  private cargarUsuarios() {
    this.http.get<User[]>('http://localhost:8085/api/user').subscribe(
      (datos: User[]) => {
       this.usuarios=datos;
        this.usuariosSubject.next(this.usuarios);
      },
      error => {
        console.error('Error al cargar los estudiantes:', error);
      }
    );
  }


  getUser(id:number):Observable<User>{
  
    
    return this.http.get<User>(environment.API_BACKEND_URL_USER+id).pipe(

      catchError(this.handleError)
    );

  }
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Error desconocido';
    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del lado del servidor
      errorMessage = `Error: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(errorMessage); // Lanza el error en lugar de devolver un Observable<string>
  }


  getUsuarios(): Observable<User[]>{

return this.usuariosSubject.asObservable();
  }


  editarUsuario(user: any): Observable<User> {
    const url = `${this.apiUrl}/cambiarUser/${user.id}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<User>(url, user, { headers }).pipe(
      tap(updatedUser => {
        // Actualizar el usuario en la lista local y emitir el nuevo valor
        const index = this.usuarios.findIndex(u => u.id === updatedUser.id);
        if (index !== -1) {
          this.usuarios[index] = updatedUser;
          this.usuariosSubject.next(this.usuarios);
        }
      }),
      catchError(this.handleError)
    );
  }
  
  eliminarUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/eliminarUsuario/${id}`, { responseType: 'text' as 'json' }).pipe(
 tap(respuesta=>{

  this.cargarUsuarios();
 }),catchError(this.handleError)

    );
  }

  agregarUser(user: any): Observable<User> {
    return this.http.post<User>(environment.BACKEND_URL + `auth/register`, user).pipe(
      tap(respuesta => {
        this.cargarUsuarios();
      //  this.usuarios.push(nuevoUser);
       // this.usuariosSubject.next(this.usuarios);
      }),
      catchError(this.handleError)
    );
  }


  behaviorSubjecUsuario(): Observable<User[]> {
    return this.usuariosSubject.asObservable();
  }
  

  obtenerUsuarios(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl).pipe(
      tap(usuarios => this.usuariosSubject.next(usuarios)), // Actualizar el BehaviorSubject
      catchError(this.handleError)
    );
  }

  

}

