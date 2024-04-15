import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comidas } from '../modelos/comidas';

@Injectable({
  providedIn: 'root'
})
export class ComidaService {

  constructor(private http:HttpClient) { }


  getComidas():Observable<Comidas[]>{


    return this.http.get<Comidas[]>('http://localhost:8085/api/comidas');


  }
}
