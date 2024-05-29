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
  //protocolo que se usara para enviar mensajes
  private stompClient!: Stomp.Client;
  private token: string | null = null; // Inicialmente, el token es null

  constructor(private loginService: LoginService) {
    this.token = sessionStorage.getItem('token'); // Obtener el token almacenado en sessionStorage
    this.connect(); // Conectar al WebSocket
  }
//connect esatablece la conexion con el servidor websocket 
public connect(): void {
  if (!this.token) {
    console.error('Token de autenticación no encontrado.');
    return;
  }

  const socket = new SockJS('http://localhost:8085/ws');
  this.stompClient = Stomp.over(socket);

  this.stompClient.connect(
    { Authorization: `Bearer ${this.token}` },
    (frame) => {
      console.log('Conectado al WebSocket:', frame);
      // Re-suscribirse al tópico después de la conexión exitosa
      this.subscribeToAsistencias().subscribe(
        (asistencia) => {
          console.log('Recibido en subscribeToAsistencias durante connect:', asistencia);
        },
        (error) => {
          console.error('Error en subscribeToAsistencias durante connect:', error);
        }
      );
    },
    (error) => {
      console.error('Error al conectar al WebSocket', error);
    }
  );
}


  // Métodos para enviar y suscribirse a mensajes en el WebSocket...


//envia un mensaje al wesocket
  public sendAsistencia(asistencia: Asistencia): void {
    this.stompClient.send('/app/nuevaAsistencia', {}, JSON.stringify(asistencia));
  }
  //se subscribe para recibir mensajes de la ruta /topic/messages

  public subscribeToAsistencias(): Observable<Asistencia> {
    const subject = new Subject<Asistencia>();
    this.stompClient.subscribe('/topic/asistencias', (message: { body: string }) => {
      subject.next(JSON.parse(message.body) as Asistencia);
    });
    return subject.asObservable();
  }
}
