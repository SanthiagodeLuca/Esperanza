import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Estudiante } from '../modelos/estudiante';

@Injectable({
  providedIn: 'root'
})
export class EstudianteService {

  constructor(private http:HttpClient) { }
//inyectamos el servicio httpclient

//devolvemos un tipo observable que emite un array de estudiantes 
  getEstudiantes():Observable<Estudiante[]>{

    //hace una solicitud GET al backend
    //usa HttpClient para hacer la solicitud
    //Devuelve un observable 
    return this.http.get<Estudiante[]>('http://localhost:8085/api/estudiantes');
  }
}
