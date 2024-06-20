import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Asistencia } from '../modelos/asistencia';
import { filterAsistencia } from '../modelos/filterAsistencia';
import { AsistenciaNueva } from '../modelos/asistenciaNueva';
@Injectable({
  providedIn: 'root'
})
export class AsistenciaService {
  private apiUrl = 'http://localhost:8085/api/asistencias';
 
  constructor(private http:HttpClient) { }

getAsistencias():Observable<Asistencia[]>{

  return this.http.get<Asistencia[]>('http://localhost:8085/api/asistencias');
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

  return this.http.post<Asistencia[]>(url, asistenciaFilter, { headers });
}
editarAsistencia(asistencia: any): Observable<any> {
  const url = `${this.apiUrl}/cambiarAsistencia/${asistencia.id}`;
  const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  return this.http.put<any>(url, asistencia, { headers });
}

eliminarAsistencia(id:any):Observable<void>{
//backen devuelve el cuerpo vacio 

  const url = `${this.apiUrl}/eliminarAsistencia/${id}`;

  return this.http.delete<void>(url, { responseType: 'text' as 'json' });



}

}
