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

  constructor(private userService: UserService, private userRoleService: UserRoleService) {}

  ngOnInit() {
    this.subscription = this.userService.behaviorSubjecUsuario().subscribe(
      datos => {
        this.usuarios = datos;
        console.log(this.usuarios)
        this.usuariosFiltrados = this.filtrarUsuarios();
        this.getUniqueValues();
      }
    );

    this.userRoleService.currentUser$.subscribe(user => {
      this.currentUserRole = user && user.role !== undefined ? user.role : null;
    });
  }

  ngOnDestroy() {
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

  cancelarEdicion() {
    this.mostrarFormularioUsuarioEdicion = false;
    this.usuarioParaEditar = null;
  }

  cancelarEliminacion() {
    this.mostrarAlertaEliminar = false;
    this.usuarioAEliminar = null;
  }

  confirmarEliminacion(usuario: User) {
    this.usuarioAEliminar = usuario;
    this.mostrarAlertaEliminar = true;
  }

  eliminarUsuario() {
    if (this.usuarioAEliminar) {
      this.userService.eliminarUsuario(this.usuarioAEliminar.id).subscribe(
        response => {
          console.log(response);
          this.cancelarEliminacion();
          this.usuarios = this.usuarios.filter(user => user.id !== this.usuarioAEliminar?.id);
          this.usuariosFiltrados = this.filtrarUsuarios();
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
      const idCumple = !this.filtros['id'] || usuario.id.toString().includes(this.filtros['id']);
      const nombreCumple = !this.filtros['firstname'] || (usuario.firstname && usuario.firstname.toLowerCase().includes(this.filtros['firstname'].toLowerCase()));
      const rolCumple = !this.filtros['role'] || usuario.role === this.filtros['role'];
      const paisCumple = !this.filtros['country'] || usuario.country === this.filtros['country'];
      return idCumple && nombreCumple && rolCumple && paisCumple;
    });
  }

  getUniqueValues() {
    this.uniqueValues['id'] = [...new Set(this.usuarios.map(user => user.id))];
    this.uniqueValues['role'] = [...new Set(this.usuarios.map(user => user.role))];
    this.uniqueValues['country'] = [...new Set(this.usuarios.map(user => user.country))];
  }

  limpiarFiltros() {
    this.filtros = {}; // Resetea todos los filtros a un objeto vacío
    this.usuariosFiltrados = this.filtrarUsuarios(); // Restablece la lista filtrada a la lista completa
  }
  
}
