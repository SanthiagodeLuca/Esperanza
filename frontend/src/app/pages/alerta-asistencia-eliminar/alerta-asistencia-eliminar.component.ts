import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-alerta-asistencia-eliminar',

  templateUrl: './alerta-asistencia-eliminar.component.html',
  styleUrl: './alerta-asistencia-eliminar.component.scss'
})
export class AlertaAsistenciaEliminarComponent {
  @Input() message: string = '¿Está seguro de que quiere eliminar este Asistencia?';
  @Input() confirmButtonText: string = 'Sí, eliminarlo';
  @Input() cancelButtonText: string = 'Cancelar';
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onConfirm() {
    this.confirm.emit();
  }

  onCancel() {
    this.cancel.emit();
  }
}
