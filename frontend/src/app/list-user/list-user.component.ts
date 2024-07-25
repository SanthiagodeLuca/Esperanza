import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/modelos/user';
import { UserService } from 'src/app/services/user/user.service';
import { UserRoleService } from '../services/userRole/user-role.service';

@Component({
  selector: 'app-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.scss']
})
export class ListUserComponent implements OnInit, OnDestroy {
  usuarios: User[] = [];
  uniqueValues: { [key: string]: any[] } = {};
  filtros: { [key: string]: any } = {};

  usuarioParaEditar: User | null = null;
  mostrarFormularioUsuarioEdicion: boolean = false;
  private subscription: Subscription | null = null;

  usuariosFiltrados: User[] = [];
  usuarioAEliminar: User | null = null;
  mostrarAlertaEliminar: boolean = false;

  currentUserRole: string | null = null;

  constructor(private userService: UserService,private userRoleService:UserRoleService) {}

  ngOnInit() {
    this.subscription = this.userService.behaviorSubjecUsuario().subscribe(
      datos => {
        this.usuarios = datos;
        console.log(this.usuarios)
        this.usuariosFiltrados = this.filtrarUsuarios();
      //  this.getUniqueValues();
      }
    );

    this.userRoleService.currentUser$.subscribe(user => {
      this.currentUserRole = user && user.role !== undefined ? user.role : null;
    });
  }

  ngOnDestroy() {
    // Cancelar la suscripción para evitar fugas de memoria
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }


  editarUsuario(usuario: User) {
    this.usuarioParaEditar = usuario;
    this.mostrarFormularioUsuarioEdicion = true;
  }

  guardarUsuario(user: User) {
    this.userService.editarUsuario(user).subscribe(
      response => {
        this.usuarioParaEditar = null;
        this.mostrarFormularioUsuarioEdicion = false;
      },
      error => {
        console.error('Error al actualizar el usuario:', error);
      }
    );
  }

  cancelarEliminacion() {
    this.mostrarFormularioUsuarioEdicion = false;
    this.usuarioParaEditar = null;
    this.mostrarAlertaEliminar = false; // Ensure this is reset
    this.usuarioAEliminar = null; // Reset the user to be deleted
  }

  confirmarEliminacion(usuario: User) {
    this.usuarioAEliminar = usuario;
    this.mostrarAlertaEliminar = true;
  }
  eliminarAsistencia() {
    if (this.usuarioAEliminar) {
      this.userService.eliminarUsuario(this.usuarioAEliminar.id).subscribe(
        response => {
          console.log(response); // Manejar la respuesta en texto plano
          this.cancelarEliminacion(); // Hide the alert and reset the deletion state
          this.usuarios = this.usuarios.filter(user => user.id !== this.usuarioAEliminar?.id);
          this.usuariosFiltrados = this.filtrarUsuarios(); // Update filtered list
        },
        error => {
          console.error('Error al eliminar el usuario:', error);
        }
      );
    }
  }
 

  isUserRole(role: string): boolean {
    return this.currentUserRole === role;
  }

  aplicarFiltros() {
    this.usuariosFiltrados = this.filtrarUsuarios();
  }

  filtrarUsuarios() {
    return this.usuarios.filter(usuario => {
      const nombreCumple = !this.filtros['firstname'] || (usuario.firstname && usuario.firstname.toLowerCase().includes(this.filtros['firstname'].toLowerCase()));
      const rolCumple = !this.filtros['role'] || usuario.role === this.filtros['role'];
      return nombreCumple && rolCumple;
    });
  }
  
}
