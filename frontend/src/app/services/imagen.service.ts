import { HttpClient } from '@angular/common/http';
import { EnvironmentInjector, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL_IMAGEN } from '../shared/constantes';
import { environment } from 'src/enviroments/enviroments';
import { Estudiante } from '../modelos/estudiante';

@Injectable({
  providedIn: 'root'
})
export class ImagenService {

  
  constructor(private http:HttpClient) {}
/*  obtenerImagen(nombreImagen:string):Observable<string>{
    //{reponseType:'text'} ,pasa un objeto como parametro para decir que la respuesta sea texto
    return this.http.get('http://localhost:8085/api/imagen'+'/'+nombreImagen,{responseType:'text'});

  }*/

  getUrlImagen(estudiante: Estudiante): Observable<string> {
    // Realizar una solicitud HTTP para obtener la URL de la imagen
    return this.http.get<string>(/*environment.API_URL_IMAGEN*/'http://localhost:8085/api/imagen/image/'+estudiante.id+'.png');
  }

  
}
