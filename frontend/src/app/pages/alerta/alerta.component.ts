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
  message: string | null = null;
  asistencia: any | null = null;
  asistencias: any[] = [];
  private asistenciaSubscription: Subscription | undefined;

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.asistenciaSubscription = this.webSocketService.subscribeToAsistencias().subscribe(
      (asistencia: any) => {
        this.asistencia = asistencia;
        this.asistencias.push(asistencia);
        this.showMessage(`Asistencia agregada correctamente: ${asistencia.estudiante.nombre}`);
      },
      (error) => {
        console.error('Error en la suscripción a las asistencias:', error);
      }
    );
  }

  ngOnDestroy(): void {
    if (this.asistenciaSubscription) {
      this.asistenciaSubscription.unsubscribe();
    }
  }

  showMessage(message: string): void {
    this.message = message;
    setTimeout(() => {
      this.message = null;
    }, 3000); // Oculta el mensaje después de 3 segundos
  }
}
