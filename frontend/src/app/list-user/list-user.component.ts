import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/modelos/user';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.scss']
})
export class ListUserComponent implements OnInit {
  
  usuarios: any[] = []; 
  uniqueValues: any = {};
  filtros: { [key: string]: any } = {};

  usuarioParaEditar:User| null = null;
  mostrarFormularioUsuarioEdicion: boolean = false;
  private subscription: Subscription | null = null; // Inicializar con null

  usuariosFiltrados:User[] = []; 

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.subscription=this.userService.behaviorSubjecUsuario().subscribe(

      datos=>{

        this.usuarios=datos;
        console.log(this.usuarios)
        this.usuariosFiltrados = JSON.parse(JSON.stringify(this.usuarios));

      }
    );
  

  }
   ngOnDestroy() {
    // Cancelamos la suscripción para evitar fugas de memoria
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  getUniqueValues() {
    this.usuarios.forEach(usuario => {
      Object.keys(usuario).forEach(key => {
        if (!this.uniqueValues[key]) {
          this.uniqueValues[key] = [];
        }
        if (!this.uniqueValues[key].includes(usuario[key])) {
          this.uniqueValues[key].push(usuario[key]);
        }
      });
    });
  }

  editarUsuario(usuario: any) {
    
    console.log('Editando user:', usuario);
    this.usuarioParaEditar = usuario;
    this.mostrarFormularioUsuarioEdicion = true; // Mostrar el formulario de edición


  }
  guardarUsuario(user:any){
    this.userService.editarUsuario(user).subscribe(

      response=>{

        this.usuarioParaEditar=null;
        this.mostrarFormularioUsuarioEdicion=false;
       //obtener usuarios this.

      },
      error => {
        console.error('Error al actualizar la usuarios:', error);
      }

    );

  }

  cancelarEliminacion(evento:any){
    this.mostrarFormularioUsuarioEdicion=false;
    this.usuarioParaEditar=null;
  }
  confirmarEliminacion(id: number) {
    this.userService.eliminarUsuario(id).subscribe({
      next: (response: string) => {
        console.log(response); // Manejar la respuesta en texto plano
        this.obtenerUsuarios(); // Refrescar la lista de usuarios
      },
      error: (error) => {
        console.error('Error al eliminar el usuario:', error);
      }
    });
  }
  
   //this.mostrarAlertaEliminar = false;
  
  

  aplicarFiltros() {
    // Lógica para aplicar filtros a los usuarios
  }

  filtrarUsuarios() {
    return this.usuarios.filter(usuario => {
      // Verificar si el nombre cumple con el filtro
      const nombreCumple = !this.filtros['firstname'] || usuario.firstname.toLowerCase().includes(this.filtros['firstname'].toLowerCase());
      
      // Verificar si el rol cumple con el filtro
      const rolCumple = !this.filtros['role'] || usuario.role === this.filtros['role'];
  
      // Devolver verdadero solo si todos los filtros se cumplen
      return nombreCumple && rolCumple;
    });
  }
  
obtenerUsuarios() {
  this.userService.getUsuarios().subscribe(
    usuarios => {
      this.usuarios = usuarios;
    },
    error => {
      console.error('Error al obtener la lista de estudiantes:', error);
    }
  );
}
}
