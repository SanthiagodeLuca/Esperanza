import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { User } from 'src/app/modelos/user';

@Component({
  selector: 'app-formulario-usuario',
 
  templateUrl: './formulario-usuario.component.html',
  styleUrl: './formulario-usuario.component.scss'
})
export class FormularioUsuarioComponent {
  @Input() usuario: any | null = null; // Cambiado a 'usuario' en lugar de 'estudiante'
  @Output() onSave = new EventEmitter<any>();
  @Output() onCancel = new EventEmitter<any>();
  @Input() mostrarFormularioUsuarioEdicion: boolean = false;

  ngOnChanges(changes: SimpleChanges): void {
    // Verificar si la propiedad 'usuario' cambió
    if (changes['usuario']) {
      console.log('Usuario cambió:', this.usuario);
    }
  }

  save() {
    if (this.usuario) {
      const updatedUsuario: any = {
        firstname: this.usuario.firstname,
        lastname: this.usuario.lastname,
        role: this.usuario.role,
        country: this.usuario.country,
        username: this.usuario.username,
        id: this.usuario.id
      };

      this.onSave.emit(updatedUsuario);
    }
  }

  cancelarEdicion() {
    this.mostrarFormularioUsuarioEdicion=false;
    this.onCancel.emit();
  }
}
