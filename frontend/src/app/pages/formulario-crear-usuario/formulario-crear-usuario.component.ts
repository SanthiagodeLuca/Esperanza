  import { Component, EventEmitter, Input, Output } from '@angular/core';
  import { User } from 'src/app/modelos/user';
  import { UserService } from 'src/app/services/user/user.service';

  @Component({
    selector: 'app-formulario-crear-usuario',

    templateUrl: './formulario-crear-usuario.component.html',
    styleUrl: './formulario-crear-usuario.component.scss'
  })
  export class FormularioCrearUsuarioComponent {

    @Input() mostrarFormulario: boolean = false;
    @Output() onClose = new EventEmitter<void>();
    @Output() onUsuarioGuardado = new EventEmitter<void>();

    usuario: any = {
    
      firstname:'' ,
      lastname:'',
      username:'' ,
      id:0 ,
      country:'',
      password:'',
      role:'ADMIN'
    };

    constructor(private usuarioService: UserService) {}

    guardarUsuario() {
      this.usuarioService.agregarUser(this.usuario).subscribe(
        response => {
          console.log('Usuario guardado:', response);
          this.onUsuarioGuardado.emit(); // Emitir evento cuando el usuario se guarda

          this.cerrarFormulario();
        },
        error => {
          console.error('Error al guardar el estudiante:', error);
        }
      );  
    }

    cerrarFormulario() {
      this.mostrarFormulario = false;
      this.onClose.emit();
    }



  }
