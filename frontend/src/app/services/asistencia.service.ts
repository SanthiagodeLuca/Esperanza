import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Asistencia } from '../modelos/asistencia';
import { filterAsistencia } from '../modelos/filterAsistencia';
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

}
