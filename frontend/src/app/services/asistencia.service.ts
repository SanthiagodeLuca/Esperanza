import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Asistencia } from '../modelos/asistencia';
import { filterAsistencia } from '../modelos/filterAsistencia';
import { AsistenciaNueva } from '../modelos/asistenciaNueva';
@Injectable({
  providedIn: 'root'
})
export class AsistenciaService {
  private apiUrl = 'http://localhost:8085/api/asistencias';
  private asistencias: any[] = [];

  private asistenciasSubject: BehaviorSubject<any[]> = new BehaviorSubject(this.asistencias);

  constructor(private http:HttpClient) { 
    this.cargarAsistencias();
  }

getAsistencias():Observable<Asistencia[]>{

  return this.http.get<Asistencia[]>('http://localhost:8085/api/asistencias');
}

private cargarAsistencias() {
  this.http.get<any[]>('http://localhost:8085/api/asistencias').subscribe(
    (asistencias: any[]) => {
 this.asistencias=asistencias;
      this.asistenciasSubject.next(asistencias);
    },
    error => {
      console.error('Error al cargar los estudiantes:', error);
    }
  );
}
obtenerAsistencias(filter: filterAsistencia): Observable<Asistencia[]> {
  const url = `${this.apiUrl}/fecha`;
  const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

  // Convertir el objeto filterAsistencia a AsistenciaFilter
  //esto hace que lo permita leer el backend
  const asistenciaFilter: any = {
    startDate: filter.startDate,
    endDate: filter.endDate
  };

  return this.http.post<Asistencia[]>(url, asistenciaFilter, { headers }).pipe(
    tap((asistencias: Asistencia[]) => {
      this.asistencias = asistencias;
      this.asistenciasSubject.next(asistencias);
    })
  );;
}
editarAsistencia(asistencia: any): Observable<any> {
  const url = `${this.apiUrl}/cambiarAsistencia/${asistencia.id}`;
  const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  return this.http.put<any>(url, asistencia, { headers }).pipe(
    tap((nuevaAsistencia: any) => {
    const AsistenciasActuales=this.asistenciasSubject.getValue();
    const Todos = [...AsistenciasActuales, nuevaAsistencia];
    this.asistenciasSubject.next(Todos);

    })  
    );
}

eliminarAsistencia(id:any):Observable<void>{
//backen devuelve el cuerpo vacio 

  const url = `${this.apiUrl}/eliminarAsistencia/${id}`;

  return this.http.delete<void>(url, { responseType: 'text' as 'json' });

}
behaviorSubjectEstudiantes(): Observable<any[]> {
  return this.asistenciasSubject.asObservable();
}

}
