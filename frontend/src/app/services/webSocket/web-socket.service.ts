import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
import { Asistencia } from 'src/app/modelos/asistencia';
import { LoginService } from '../auth/login.service';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient!: Stomp.Client;
  private token: string | null = null;
  private connectionPromise!: Promise<void>;

  constructor(private loginService: LoginService) {
    this.token = sessionStorage.getItem('token');
    this.connectionPromise = this.connect();
  }

  private connect(): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      if (!this.token) {
        console.error('Token de autenticación no encontrado.');
        reject('Token de autenticación no encontrado.');
        return;
      }

      const socket = new SockJS('http://localhost:8085/ws');
      this.stompClient = Stomp.over(socket);

      this.stompClient.connect({ Authorization: `Bearer ${this.token}` }, (frame) => {
        console.log('Conectado al WebSocket:', frame);
        resolve();
      }, (error) => {
        console.error('Error al conectar al WebSocket', error);
        reject(error);
      });
    });
  }

  public sendAsistencia(asistencia: Asistencia): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.send('/app/nuevaAsistencia', {}, JSON.stringify(asistencia));
    } else {
      console.error('No conectado al WebSocket. No se puede enviar asistencia.');
    }
  }

  public subscribeToAsistencias(): Observable<Asistencia> {
    const subject = new Subject<Asistencia>();

    this.connectionPromise.then(() => {
      this.stompClient.subscribe('/topic/asistencias', (message: { body: string }) => {
        subject.next(JSON.parse(message.body) as Asistencia);
      });
    }).catch((error) => {
      console.error('Error al suscribirse a las asistencias:', error);
    });

    return subject.asObservable();
  }
}
