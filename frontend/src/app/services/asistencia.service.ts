import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Asistencia } from '../modelos/asistencia';
@Injectable({
  providedIn: 'root'
})
export class AsistenciaService {

 
  constructor(private http:HttpClient) { }

getAsistencias():Observable<Asistencia[]>{

  return this.http.get<Asistencia[]>('http://localhost:8085/api/asistencias');
}
}
