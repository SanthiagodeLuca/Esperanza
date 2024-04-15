import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL_IMAGEN } from '../shared/constantes';

@Injectable({
  providedIn: 'root'
})
export class ImagenService {

  
  constructor(private http:HttpClient) {}
/*  obtenerImagen(nombreImagen:string):Observable<string>{
    //{reponseType:'text'} ,pasa un objeto como parametro para decir que la respuesta sea texto
    return this.http.get('http://localhost:8085/api/imagen'+'/'+nombreImagen,{responseType:'text'});

  }*/

  getUrlImagen(): Observable<string> {
    // Realizar una solicitud HTTP para obtener la URL de la imagen
    return this.http.get<string>(API_URL_IMAGEN);
  }
}
