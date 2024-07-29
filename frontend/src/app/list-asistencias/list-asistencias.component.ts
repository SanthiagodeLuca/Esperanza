import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AsistenciaService } from '../services/asistencia.service'; // Importa el servicio de asistencia
import { Asistencia } from '../modelos/asistencia'; // Importa el modelo de asistencia
import { FiltroService } from '../services/filtro.service';
import { Subscription, take } from 'rxjs';
import { NotificacionService } from '../services/notificacion/notificacion.service';
import { WebSocketService } from '../services/webSocket/web-socket.service';
import { AsistenciaNueva } from '../modelos/asistenciaNueva';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { UserRoleService } from '../services/userRole/user-role.service';

@Component({
  selector: 'app-list-asistencias',
  templateUrl: './list-asistencias.component.html',
  styleUrls: ['./list-asistencias.component.scss'],
  providers: [DatePipe] // Asegúrate de incluir DatePipe en los providers del componente

})
export class ListAsistenciasComponent implements OnInit {

  

  asistencias: Asistencia[] = []; // Cambia el tipo de datos a Asistencia[]
  asistenciaFiltrada: Asistencia[] = [];
  asistenciaTabla:any[]=[];
  uniqueValues: string[] = [];
  filtros: { [key: string]: any } = {
    'id': '',
    'estudiante.id': '',
    'estudiante.nombre': '',
    'estudiante.curso': '',
    'almuerzo.nombre': '',
    'fecha': ''
  };
  usuarioRol: string = 'ADMIN'; // Suponiendo que obtienes el rol del usuario de algún servicio o componente
  asistenciaParaEditar: AsistenciaNueva | null = null;
  mostrarFormularioEdicion: boolean = false; // Variable para controlar la visibilidad del formulario de edición
  mostrarAlertaEliminar:boolean=false;
  private subscription: Subscription | null = null; // Inicializar con null
asistenciaAEliminar: number | null = null;

currentUserRole: string | null = null;


  constructor(private datePipe:DatePipe,private asistenciaService: AsistenciaService,private filtroService:FiltroService, private notificationService: NotificacionService
    ,private webSocket:WebSocketService,private cdr: ChangeDetectorRef,private router:Router,private userRoleService:UserRoleService
  ) {}

  ngOnInit(): void {
    // Rol del usuario
    this.userRoleService.currentUser$.subscribe(user => {
      this.currentUserRole = user && user.role !== undefined ? user.role : null;
    });
  
    this.subscription = this.asistenciaService.behaviorSubjectEstudiantes().subscribe(data => {
      this.asistencias = data;
  
      // Ordenar las asistencias por ID de forma descendente
      this.asistencias.sort((a, b) => b.id - a.id);
      
      this.asistenciaTabla = this.asistencias;
      this.asistenciaFiltrada = JSON.parse(JSON.stringify(this.asistencias)); // Clona los datos para filtrar
    });
  }
  
ngOnDestroy(){

  if (this.subscription) {
    this.subscription.unsubscribe();
  }
}

 isUserRole(role: string): boolean {
      return this.currentUserRole === role;
    }
  getUniqueValues(column: string): string[] {
    // Divide la cadena de la columna en las propiedades anidadas
    const properties = column.split('.');
    // Inicializa un array de datos con los datos filtrados de asistenciaFiltrada
    let data: any[] = this.asistenciaFiltrada;
  
    // Itera sobre las propiedades anidadas para acceder a los valores
    for (const prop of properties) {
      // Por cada propiedad, se mapea el array de datos para acceder a la propiedad especificada
      data = data.map(item => item[prop]);
    }

    // Formatea la fecha para obtener valores únicos de solo fecha (sin tiempo)
    if (column === 'fecha') {
      data = data.map(date => this.datePipe.transform(date, 'yyyy-MM-dd'));
    }


  
    // Filtra los valores únicos utilizando un conjunto para eliminar duplicados y luego los convierte nuevamente en una lista
    return [...new Set(data)];
  }
  

  


  
  get filtrarAsistencia(): Asistencia[] {
    return this.filtroService.aplicarFiltros2(this.asistencias, this.filtros);
  } 

  aplicarFiltros() {
    console.log('Filtros aplicados:', this.filtros);
    console.log('Asistencias originales:', this.asistenciaTabla);
    console.log('Filtro ID:', this.filtros['id']); // Add this line
  
    this.asistenciaFiltrada = this.asistenciaTabla.filter(asistencia => {
      // Convert the filter value to a number if necessary
      const idFilter = !this.filtros['id'] || asistencia.id === +this.filtros['id']; // Convert filter value to a number
      const fechaFilter = !this.filtros['fecha'] || this.datePipe.transform(asistencia.fecha, 'yyyy-MM-dd') === this.filtros['fecha'];
      const estudianteIdFilter = !this.filtros['estudiante.id'] || this.matchNestedProperty(asistencia, 'estudiante.id', this.filtros['estudiante.id']);
      const estudianteNombreFilter = !this.filtros['estudiante.nombre'] || this.matchNestedProperty(asistencia, 'estudiante.nombre', this.filtros['estudiante.nombre']);
      const cursoFilter = !this.filtros['estudiante.curso'] || this.matchNestedProperty(asistencia, 'estudiante.curso', this.filtros['estudiante.curso']);
      const comidasFilter = !this.filtros['almuerzo.nombre'] || this.matchNestedProperty(asistencia, 'almuerzo.nombre', this.filtros['almuerzo.nombre']);
  
      console.log(`ID Filter: ${idFilter}, Fecha Filter: ${fechaFilter}, Estudiante ID Filter: ${estudianteIdFilter}, Curso Filter: ${cursoFilter}, Comidas Filter: ${comidasFilter}`);
      
      return idFilter && fechaFilter && estudianteIdFilter && estudianteNombreFilter && cursoFilter && comidasFilter;
    });
  
    console.log('Asistencias filtradas:', this.asistenciaFiltrada);
  }
  
  

  matchNestedProperty(object: any, propertyPath: string, value: any): boolean {
    const properties = propertyPath.split('.');
    let nestedObject = object;
  
    for (const prop of properties) {
      if (nestedObject[prop] === undefined) {
        console.log(`Property '${prop}' not found`);
        return false;
      }
      nestedObject = nestedObject[prop];
    }
  
    const result = nestedObject === value;
    console.log(`Matching ${propertyPath}: expected ${value}, got ${nestedObject}, result ${result}`);
    return result;
  }
  
  filtrarSoloPorFecha(fecha: string) {
    this.asistenciaFiltrada = this.asistenciaTabla.filter(asistencia => {
      return this.datePipe.transform(asistencia.fecha, 'yyyy-MM-dd') === fecha;
    });
  }

  limpiarFiltros() {
    this.filtros = {
      'id': '',
      'estudiante.id': '',
      'estudiante.nombre': '',
      'estudiante.curso': '',
      'almuerzo.nombre': '',
      'fecha': ''
    }; // Resetea todos los filtros a una cadena vacía
    this.asistenciaFiltrada = [...this.asistenciaTabla]; // Restablece la lista filtrada a la lista completa
  }



  agregarAsistencia() {
    this.webSocket.subscribeToAsistencias().subscribe(response => {
    console.log(response)
    });
  }

  editarAsistencia(asistencia: AsistenciaNueva) {
    console.log('Editando asistencia:', asistencia);
    this.asistenciaParaEditar = asistencia;
    this.mostrarFormularioEdicion = true; // Mostrar el formulario de edición
  }

   


  guardarAsistencia(asistencia: any) {
  //sconsole.log(asistencia)
    this.asistenciaService.editarAsistencia(asistencia).subscribe(
      response => {
        console.log('Asistencia actualizada:', response);
         // Actualiza los datos de asistencia
      
        this.asistenciaParaEditar = null;
        this.mostrarFormularioEdicion = false; // Oculta el formulario después de guardar

      },
      error => {
        console.error('Error al actualizar la asistencia:', error);
      }
    );
  }

  cancelarEdicion() {
    this.asistenciaParaEditar = null;
    this.mostrarFormularioEdicion = false; // Oculta el formulario después de cancelar

  }

  eliminarAsistencia(id: number) {

    if (this.asistenciaAEliminar) {

    this.asistenciaService.eliminarAsistencia(id).subscribe(
      () => {
        console.log('Asistencia eliminada');
        this.asistencias = this.asistencias.filter(asistencia => asistencia.id !== id);
        this.asistenciaFiltrada = this.asistenciaFiltrada.filter(asistencia => asistencia.id !== id);
        this.asistenciaTabla = this.asistenciaTabla.filter(asistencia => asistencia.id !== id);
        this.mostrarAlertaEliminar = false; // Ocultar la alerta después de eliminar

      },
      error => {
        console.error('Error al eliminar la asistencia:', error);
      }
    );}
  }
  


  confirmarEliminacion(id: number) {
    this.asistenciaAEliminar = id;
    this.mostrarAlertaEliminar = true;
  }

  cancelarEliminacion() {
    // Lógica para cancelar la eliminación del estudiante
    // Oculta la alerta de eliminación
    this.mostrarAlertaEliminar = false;
  }
  formatDate(date: string): string {
    return this.datePipe.transform(date, 'yyyy-MM-dd HH:mm:ss') || '';
  }

}