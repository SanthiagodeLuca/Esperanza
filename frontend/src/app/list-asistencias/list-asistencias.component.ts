import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AsistenciaService } from '../services/asistencia.service'; // Importa el servicio de asistencia
import { Asistencia } from '../modelos/asistencia'; // Importa el modelo de asistencia
import { FiltroService } from '../services/filtro.service';
import { Subscription, take } from 'rxjs';
import { NotificacionService } from '../services/notificacion/notificacion.service';
import { WebSocketService } from '../services/webSocket/web-socket.service';
import { AsistenciaNueva } from '../modelos/asistenciaNueva';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-asistencias',
  templateUrl: './list-asistencias.component.html',
  styleUrls: ['./list-asistencias.component.scss']
})
export class ListAsistenciasComponent implements OnInit {

  

  asistencias: Asistencia[] = []; // Cambia el tipo de datos a Asistencia[]
  asistenciaFiltrada: Asistencia[] = [];
  asistenciaTabla:any[]=[];
  uniqueValues: string[] = [];
  filtros: { [key: string]: any } = {};
  usuarioRol: string = 'admin'; // Suponiendo que obtienes el rol del usuario de algún servicio o componente
  asistenciaParaEditar: AsistenciaNueva | null = null;
  mostrarFormularioEdicion: boolean = false; // Variable para controlar la visibilidad del formulario de edición
  mostrarAlertaEliminar:boolean=false;
  private subscription: Subscription | null = null; // Inicializar con null
asistenciaAEliminar: number | null = null;

  constructor(private asistenciaService: AsistenciaService,private filtroService:FiltroService, private notificationService: NotificacionService
    ,private webSocket:WebSocketService,private cdr: ChangeDetectorRef,private router:Router
  ) {}

  ngOnInit(): void {
   this.subscription= this.asistenciaService.behaviorSubjectEstudiantes().subscribe(data => {
     
    
      
        this.asistencias = data;
        this.asistenciaTabla=data;
        console.log(this.asistencias)

     
      this.asistenciaFiltrada =JSON.parse(JSON.stringify(this.asistencias)); // Clona los datos para filtrar
    });
  }
ngOnDestroy(){

  if (this.subscription) {
    this.subscription.unsubscribe();
  }
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
  
    // Filtra los valores únicos utilizando un conjunto para eliminar duplicados y luego los convierte nuevamente en una lista
    return [...new Set(data)];
  }
  

  


  
  get filtrarAsistencia(): Asistencia[] {
    return this.filtroService.aplicarFiltros2(this.asistencias, this.filtros);
  } 

  aplicarFiltros() {
    this.asistenciaFiltrada = this.asistenciaTabla.filter(asistencia => {
      // Lógica para filtrar por cada columna
      const idFilter = !this.filtros['id'] || asistencia.id === this.filtros['id'];
      const estudianteFilter = !this.filtros['estudiante'] || asistencia.estudiante.id === this.filtros['estudiante'];
      const comidasFilter = !this.filtros['comidas'] || asistencia.almuerzo === this.filtros['comidas'];
      // Otros filtros aquí
      return idFilter && estudianteFilter && comidasFilter /* && otros filtros */;
    });
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

}
