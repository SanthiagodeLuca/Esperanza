  import { Injectable } from '@angular/core';
  import { HttpClient } from '@angular/common/http';
  import { BehaviorSubject, Observable } from 'rxjs';
  import { Horario } from 'src/app/modelos/horario';
  import { environment } from 'src/enviroments/enviroments';


  @Injectable({
    providedIn: 'root'
  })
  export class HorarioService {

    private baseUrl = 'http://localhost:8085/api/horario'; 
    breakfastData$: any;
    lunchData$: any;
    snackData$: any;
    getBreakfastSchedule(id: number): Observable<Horario> {
      return this.http.get<Horario>(`${this.baseUrl}/desayuno/${id}`);
    }

    getLunchSchedule(id: number): Observable<Horario> {
      return this.http.get<Horario>(`${this.baseUrl}/almuerzo/${id}`);
    }

    getSnackSchedule(id: number): Observable<Horario> {
      return this.http.get<Horario>(`${this.baseUrl}/refrigerio/${id}`);
    }
    private refrigerioData = new BehaviorSubject<Horario>({
      id: 0,
      startHour: 0,
      startMinute: 0,
      endHour: 0,
      endMinute: 0,
      tipoComida: ""
    });
    refrigerioData$ = this.refrigerioData.asObservable();

    private apiUrl = `${environment.BACKEND_URL}api/horario`;

    constructor(private http: HttpClient) {}

    actualizarComida(data: Horario) {
      console.log(data);
      this.refrigerioData.next(data);
    }

    enviarComida(data: Horario,id:number) {

      const url = `${this.apiUrl}/${id}`;
      this.http.put(url, data).subscribe(
        response => {
          console.log('Refrigerio enviado con éxito:', response);
        },
        error => {
          console.error('Error al enviar el refrigerio:', error);
        }
      );
    }

    enviarDesayuno(data: Horario) {
      this.http.put(this.apiUrl, data).subscribe(
        response => {
          console.log('Desayuno enviado con éxito:', response);
        },
        error => {
          console.error('Error al enviar el Desayuno:', error);
        }
      );
    }

    enviarAlmuerzo(data: Horario) {
      this.http.put(this.apiUrl, data).subscribe(
        response => {
          console.log('Desayuno enviado con éxito:', response);
        },
        error => {
          console.error('Error al enviar el Desayuno:', error);
        }
      );
    }
  }
