import { HttpClient } from '@angular/common/http'; // Importa HttpClient para realizar solicitudes HTTP
import { Injectable } from '@angular/core'; // Importa Injectable para crear un servicio inyectable
import { Criterio } from '../modelos/criterio'; // Importa el modelo de Criterio
import { Observable } from 'rxjs'; // Importa Observable de RxJS para manejar operaciones asincrónicas

@Injectable({
  providedIn: 'root' // Indica que este servicio está disponible en el nivel raíz de la aplicación
})
export class CriterioService {
  private baseUrl = 'http://localhost:8085/api/criterios'; // URL base del backend

  constructor(private http: HttpClient) { } // Constructor del servicio, inyecta HttpClient

  // Método para actualizar un criterio
  actualizarCriterio(criterio: Criterio): Observable<any> {
    const url = `${this.baseUrl}/${criterio.id}`; // Construye la URL completa para la actualización
    return this.http.put(url, criterio); // Realiza una solicitud HTTP PUT para actualizar el criterio
  }

   // Definición del método obtenerCriterios
   obtenerCriterios(): Observable<Criterio[]> {
    return this.http.get<Criterio[]>(this.baseUrl); // Suponiendo que tu backend devuelve una lista de criterios
  }

  agregarCriterio(nuevoCriterio: Criterio): Observable<Criterio> {
    return this.http.post<Criterio>(this.baseUrl, nuevoCriterio);
  }
}
