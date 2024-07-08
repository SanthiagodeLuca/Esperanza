import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Estudiante } from '../modelos/estudiante';

@Injectable({
  providedIn: 'root'
})
export class EstudianteService {
  private apiUrl = 'http://localhost:8085/api/estudiantes';
  private estudiantes: Estudiante[] = [];
  private totalEstudiantes: number = 0; // Añadir esta línea

  private estudiantesSubject: BehaviorSubject<Estudiante[]> = new BehaviorSubject(this.estudiantes);
  
  constructor(private http:HttpClient) {

    this.cargarEstudiantes();
   }
//inyectamos el servicio httpclient

//devolvemos un tipo observable que emite un array de estudiantes 
  getEstudiantes():Observable<Estudiante[]>{

    //hace una solicitud GET al backend
    //usa HttpClient para hacer la solicitud
    //Devuelve un observable 
    return this.http.get<Estudiante[]>('http://localhost:8085/api/estudiantes');
  }
  obtenerTotalEstudiantes(): Observable<number> {
    const url = `${this.apiUrl}/cambiarEstudiante/total`;

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.get<number>(`${this.apiUrl}/total`);
  }
 
  private cargarEstudiantes() {
    this.http.get<Estudiante[]>('http://localhost:8085/api/estudiantes').subscribe(
      (estudiantes: Estudiante[]) => {
        const estudiantesConImagen = estudiantes.map(estudiante => ({
          ...estudiante,
          imagenVisible: false,
          imagenUrl: `API_URL_IMAGEN${estudiante.id}.png`
        }));
        this.estudiantesSubject.next(estudiantesConImagen);
      },
      error => {
        console.error('Error al cargar los estudiantes:', error);
      }
    );
  }
  
  eliminiarEstudiantes(id:any): Observable<void> {
    return this.http.delete<any>(`${this.apiUrl}/eliminarEstudiante/${id}`,  { responseType: 'text' as 'json' });
  }


  editarEstudiantes(estudiante:any){

    const url = `${this.apiUrl}/cambiarEstudiante/${estudiante.id}`;
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<Estudiante>(url, estudiante, { headers }).pipe(
      tap(() => {
        const estudiantesActuales = this.estudiantesSubject.getValue();
        const estudiantesActualizados = estudiantesActuales.map(e => 
          e.id === estudiante.id ? { ...estudiante, imagenUrl: `API_URL_IMAGEN${estudiante.id}.png` } : e
        );
        this.estudiantesSubject.next(estudiantesActualizados);
      })
    );
  }



  agregarEstudiante(estudiante: Estudiante): Observable<Estudiante> {
    return this.http.post<Estudiante>(`${this.apiUrl}/agregar`, estudiante).pipe(
      tap((nuevoEstudiante: Estudiante) => {
        const estudiantesActuales = this.estudiantesSubject.getValue();
        const nuevoEstudianteConImagen = {
          ...nuevoEstudiante,
          imagenVisible: false,
          imagenUrl: `API_URL_IMAGEN${nuevoEstudiante.id}.png`
        };
        const estudiantesActualizados = [...estudiantesActuales, nuevoEstudianteConImagen];
        this.estudiantesSubject.next(estudiantesActualizados);
      })
    );
  }

  behaviorSubjectEstudiantes(): Observable<Estudiante[]> {
    return this.estudiantesSubject.asObservable();
  }
}
