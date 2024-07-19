import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Asistencia } from 'src/app/modelos/asistencia';
import { WebSocketService } from 'src/app/services/webSocket/web-socket.service';

@Component({
  selector: 'app-notificacion',
  templateUrl: './notificacion.component.html',
  styleUrls: ['./notificacion.component.scss']
})
export class NotificacionComponent implements OnInit, OnDestroy {

  notificationCount: number = 0;
  showNotifications: boolean = false;
  asistencia: Asistencia | null = null;
  asistencias: any[] = [];
  errorMessage: string | null = null; // Agregado para manejar mensajes de error

  private asistenciaSubscription: Subscription | undefined;

  constructor(private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    this.asistenciaSubscription = this.webSocketService.subscribeToAsistencias().subscribe(
      (data: any) => {
        console.log('Datos recibidos:', data);
        if (data.message) {
          this.errorMessage = data.message;
          console.error('Error: ' + this.errorMessage);
        } else {
          // Asegúrate de que `data` es del tipo `Asistencia` esperado
          if (data && data.id && data.estudiante) {
            this.notificationCount++;
            this.asistencia = data;
            this.asistencias.push(data);
          } else {
            console.error('Datos recibidos no tienen la estructura esperada:', data);
          }
        }
      },
      (error) => {
        console.error('Error en la suscripción a las asistencias:', error);
      }
    );
  }
/*
  ngOnInit(): void {
    console.log('Inicializando NotificacionComponent...');
    this.asistenciaSubscription = this.webSocketService.subscribeToAsistencias().subscribe(
      (asistencia: Asistencia) => {
        console.log('Nueva asistencia recibida:', asistencia);
        this.notificationCount++;
        this.asistencia = asistencia;
        this.asistencias.push(asistencia);

      },
      (error) => {
        console.error('Error en la suscripción a las asistencias:', error);
      }
    );
  }
*/
  ngOnDestroy(): void {
    if (this.asistenciaSubscription) {
      this.asistenciaSubscription.unsubscribe();
    }
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
    if (this.showNotifications) {
      this.notificationCount = 0; // Resetear el contador cuando se abran las notificaciones
    }
  }
}
