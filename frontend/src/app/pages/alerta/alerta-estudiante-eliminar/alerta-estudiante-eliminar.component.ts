import { Component, EventEmitter, Input, Output } from '@angular/core';
@Component({
  selector: 'app-alerta-estudiante-eliminar',
 
  templateUrl: './alerta-estudiante-eliminar.component.html',
  styleUrl: './alerta-estudiante-eliminar.component.scss'
})
export class AlertaEstudianteEliminarComponent {
  @Input() message: string = '¿Está seguro de que quiere eliminar este estudiante?';
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
