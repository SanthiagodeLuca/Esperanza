import { Component } from '@angular/core';
import { UserService } from '../services/user/user.service';

@Component({
  selector: 'app-seguridad',
  templateUrl: './seguridad.component.html',
  styleUrls: ['./seguridad.component.scss']
})
export class SeguridadComponent {
  mostrarFormularioEstudiante: boolean = false;
  usuarios: any[] = []; // Array to hold the list of users


constructor(private userService: UserService) {}

ngOnInit() {
  // Load the initial list of users when the component is initialized
  this.refrescarListaUsuarios();
}

  abrirFormularioCrearAdministrador(){
    this.mostrarFormularioEstudiante = true;


  }

  cerrarFormulario() {
    this.mostrarFormularioEstudiante = false;

  }

  refrescarListaUsuarios() {
    // Fetch the updated list of users from the server
    this.userService.getUsuarios().subscribe(
      (datos: any[]) => {
        this.usuarios = datos;
      },
      error => {
        console.error('Error al obtener los usuarios:', error);
      }
    );
  }

}
