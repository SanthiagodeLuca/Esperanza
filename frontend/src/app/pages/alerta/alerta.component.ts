import { Component } from '@angular/core';
import { NotificacionService } from 'src/app/services/notificacion/notificacion.service';

@Component({
  selector: 'app-alerta',
  standalone: true,
  imports: [],
  templateUrl: './alerta.component.html',
  styleUrl: './alerta.component.scss'
})
export class AlertaComponent {
  message: string | null = null;

  constructor(private notificationService: NotificacionService) {}

  ngOnInit(): void {
    this.notificationService.notifications$.subscribe((message) => {
      this.message = message;
      setTimeout(() => {
        this.message = null;
      }, 5000); // Ocultar alerta después de 5 segundos
    });
  }
}
