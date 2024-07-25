import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-alerta-user-eliminar',

  templateUrl: './alerta-user-eliminar.component.html',
  styleUrl: './alerta-user-eliminar.component.scss'
})
export class AlertaUserEliminarComponent {

    @Input() message: string = '¿Está seguro de que quiere eliminar este Usuario?';
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
