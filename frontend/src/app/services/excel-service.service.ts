import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BACKEND_URL } from '../shared/constantes';
import { Observable, catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExcelServiceService {
 
  private apiUrl = 'http://localhost:8085/api/exportar-asistencias'; // Adjust the URL as necessary

  constructor(private http:HttpClient) { }

  enviarArchivoCSV(archivo:File):Observable<any>{

    //crear diccionarios
    const formData=new FormData();
    //agrega la clave file , al archivo
    //agrega archivo a formData 
    formData.append('file',archivo);//cambio de File a file el nombre del parametro debe coincideir con el nombre del backend


    //envia solicitud post y es un observable 
    //pipe agrega operadores al observable(operadores funciones que usan los datos del observable como parametro ejemplo filter)
    //cathError maneja los errores que ocurren en la solicitud 
    return this.http.post<any>('http://localhost:8085/api/excel/subirExcel',formData).pipe(
    catchError(this.handleError)

    );
    //return this.http.post(BACKEND_URL+'subirExcel',formData);

  }
   exportExcel(startDate: string, endDate: string): Observable<Blob> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    return this.http.get('/api/excel/export', { params, responseType: 'blob' });
  }

  
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Error desconocido al procesar el archivo Excel';
    if (error.error instanceof ErrorEvent) {
      // Error del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else if (error.error && typeof error.error === 'object' && error.error.error) {
      // Error del servidor con un mensaje específico
      errorMessage = `Error al procesar el archivo Excel: ${error.error.error}`;
    } else {
      // Error del servidor sin mensaje específico
      errorMessage = `Error al procesar el archivo Excel: ${error.error || error.statusText}`;
    }
    return throwError(errorMessage);
  }
  
  
}

