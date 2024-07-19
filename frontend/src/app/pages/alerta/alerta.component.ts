import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Asistencia } from 'src/app/modelos/asistencia';
import { WebSocketService } from 'src/app/services/webSocket/web-socket.service';

@Component({
  selector: 'app-alerta',
  templateUrl: './alerta.component.html',
  styleUrls: ['./alerta.component.scss']
})
export class AlertaComponent implements OnInit, OnDestroy {
  isSuccess: boolean = true; // Añadido para manejar el estado de éxito o error

  message: string | null = null;
  asistencia: any | null = null;
  asistencias: any[] = [];
  private asistenciaSubscription: Subscription | undefined;

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.asistenciaSubscription = this.webSocketService.subscribeToAsistencias().subscribe(
      (data: any) => {
        if (data.message) {
          // Manejar mensaje de error
          this.showMessage(data.message, false);
        } else {
          // Manejar objeto Asistencia
          this.asistencia = data;
          this.asistencias.push(data);
          this.showMessage(`Asistencia agregada correctamente: ${data.estudiante.nombre}`, true);
        }
      },
      (error) => {
        console.error('Error en la suscripción a las asistencias:', error);
        this.showMessage('Error al agregar asistencia.', false);
      }
    );
  }

  ngOnDestroy(): void {
    if (this.asistenciaSubscription) {
      this.asistenciaSubscription.unsubscribe();
    }
  }

  
  showMessage(message: string, isSuccess: boolean): void {
    this.message = message;
    this.isSuccess = isSuccess;
    setTimeout(() => {
      this.message = null;
    }, 3000); // Oculta el mensaje después de 3 segundos
  }
}
