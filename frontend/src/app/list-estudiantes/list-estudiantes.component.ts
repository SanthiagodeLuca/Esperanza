  /**Ejemplo de funcion Filtros
   * 
   * estudiantes: Estudiante[] = [
  { nombre: 'Juan', jornada: 'Mañana', curso: 'A' },
  { nombre: 'María', jornada: 'Tarde', curso: 'B' },
  { nombre: 'Pedro', jornada: 'Mañana', curso: 'A' },
  { nombre: 'Laura', jornada: 'Noche', curso: 'C' },
  { nombre: 'Carlos', jornada: 'Tarde', curso: 'B' },
];

   * const filtros = {
  //nombre: 'Juan',
  jornada: 'Mañana',
  //curso: 'A'
};

   * this.filtros['jornada'] = 'Tarde';
this.aplicarFiltros();
RESULTADO
   * { nombre: 'María', jornada: 'Tarde', curso: 'B' }
{ nombre: 'Carlos', jornada: 'Tarde', curso: 'B' }

   * 
   * 
   *  */  
  
  
  import { Component, OnInit } from '@angular/core';


  import { Estudiante } from '../modelos/estudiante';
  import { EstudianteService } from '../services/estudiante.service';
  import { ComidasComponent } from '../comidas/comidas.component';
  import { Comidas } from '../modelos/comidas';
  import { ComidaService } from '../services/comida.service';
  import { ImagenService } from '../services/imagen.service';
  import { API_URL_IMAGEN } from '../shared/constantes';
import { FiltroService } from '../services/filtro.service';
import { take } from 'rxjs/operators'; // Importa el operador 'take
import { LoginService } from '../services/auth/login.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../modelos/user';
import { UserService } from '../services/user/user.service';
import { TokenInformacionService } from '../services/token/token-informacion.service';
import { Subscription } from 'rxjs';
import { UserRoleService } from '../services/userRole/user-role.service';

  @Component({
    selector: 'app-list-estudiantes',
    templateUrl: './list-estudiantes.component.html',
    styleUrls: ['./list-estudiantes.component.scss']
  })
  export class ListEstudiantesComponent implements OnInit {
    
     public user?:User;

     authorities:string[] =[];

    estudiantes:Estudiante[]=[];
    estudiantesFiltrados: Estudiante[] = []; 
    uniqueValues: string[] = []; 
    imagenUrl:string ="";
    imagenVisible: boolean = false;

    selectedNombre: string="";
    selectedJornada: string="";
    selectedCurso: string="";

    tokenSeleccionado:string="";
    // Filtros
    filtros: { [key: string]: any } = {
      id: '',
      nombre: '',
      jornada: '',
      curso: ''
    };


    usuarioRol: string = 'admin'; // Suponiendo que obtienes el rol del usuario de algún servicio o componente
    estudianteParaEditar: Estudiante | null = null;
    mostrarFormularioEdicion: boolean = false; // Variable para controlar la visibilidad del formulario de edición

    private subscription: Subscription | null = null; // Inicializar con null
    estudianteAEliminar: string | null = null;
    mostrarAlertaEliminar:boolean=false;

    currentUserRole: string | null = null;



    constructor(private estudiantesService:EstudianteService,
      private imagenService:ImagenService,private filtroService:FiltroService,private loginService:LoginService
    ,private http:HttpClient,private userService:UserService,private tokenInformacionService:TokenInformacionService,private userRoleService:UserRoleService){}
    data=this.loginService.currentUserData;
    //metodo cunado se inica el componente
    ngOnInit(): void {
      //rol del usuario
      this.userRoleService.currentUser$.subscribe(user => {
        this.currentUserRole = user && user.role !== undefined ? user.role : null;
      });

      this.subscription = this.estudiantesService.behaviorSubjectEstudiantes().subscribe(estudiantes => {
        this.estudiantes = estudiantes;
        // Actualiza estudiantesFiltrados cada vez que estudiantes cambie
        this.estudiantesFiltrados = JSON.parse(JSON.stringify(this.estudiantes));
      });
      //se llama al metodo de estudiante sevice 
      //se subcribe(observador esta atengo a cualquier datos que envie el observable) al observable 
      //se rellena a los estudiantes(datos) el array con los datos que se tienen  
      //internamente en subcribe cuando se hace un callback el pasa el valor como paremetro de la funcion callback en este caso
      //data pasa a tener el valor del array estudiantes y aqui solo lo inicializamoss
    // (1) this.estudiantesService.getEstudiantes().pipe(take(1)).subscribe(data => {
     
        // OPCION  EJECUTAR UNA VEZ this.estudiantesService.getEstudiantes().pipe(take(1)).subscribe(data => {
        //.subcribe se subscribe al observable 

        //aqui en http subcribe ya recibio los datos 
        //lo que haces con el data.map es cambiar los datos que ya tienes y agregar un nuevo elemento
        //... indica que copia todas las propiedades del objeto original ademas se agregan propiedades
       
     //(2)   this.estudiantes = data.map(estudiante=>({...estudiante,imagenVisible:false,imagenUrl:API_URL_IMAGEN+estudiante.id+'.png'}));
        // como es asincronico las demas lineas de codigo se ejecutan mientras se espera toda la respuesta 
        // debido a que la respuesta aun no esta lista no guarda nada 

      //(3)  this.estudiantesFiltrados = JSON.parse(JSON.stringify(this.estudiantes));

     //(4) });
     // this.estudiantesFiltrados = JSON.parse(JSON.stringify(this.estudiantes));
     //this.estudiantesFiltrados = this.estudiantes.slice();

     this.cargarImagen();
     this.obtenerAuthoritiesUser();
    /* this.estudiantesService.behaviorSubjectEstudiantes().subscribe((estudiantes: Estudiante[]) => {
      this.estudiantes = estudiantes;
    });*/
    
    // this.obtenerImagen();
    }
    isUserRole(role: string): boolean {
      return this.currentUserRole === role;
    }

    ngOnDestroy() {
      // Cancelamos la suscripción para evitar fugas de memoria
      if (this.subscription) {
        this.subscription.unsubscribe();
      }
    }
    obtenerAuthoritiesUser(){

     this.authorities= this.tokenInformacionService.geAuthoritiesFromToken();
      console.log(this.authorities)
    // Ciclo for para recorrer el array authorities
        for (let i = 0; i < this.authorities.length; i++) {
          console.log(this.authorities[i]); // Muestra cada permiso en la consola
        }
    }

    cambiarEstadoImagen(estudiante: Estudiante) {
      estudiante.imagenVisible = !estudiante.imagenVisible;
      this.estudiantes = [...this.estudiantes]; // Forzar a Angular a detectar cambios
      console.log('Estudiante después de cambiar estado:', estudiante);
      if(estudiante.imagenVisible){
        this.obtenerImagen(estudiante);

      }
    }
  
    cambiarEstadoTexto(estudiante: Estudiante): string {

      
      const texto=estudiante.imagenVisible ? 'Ocultar imagen' : 'Mostrar imagen';
      console.log('Texto del botón:', texto);
      return texto;
    }
  
    mostrarImagen(estudiante:Estudiante) {
    
        this.imagenUrl = API_URL_IMAGEN + estudiante.id + '.png';

        estudiante.imagenVisible = true; // Muestra la imagen cuando se recibe la URL
      }
      cargarImagen() {
        this.loginService.userToken.subscribe(
          (token: String) => {
            console.log('Token recibido:', token);
            // Asignar el token a la propiedad tokenSeleccionado
            this.tokenSeleccionado = token.toString();
            // Otro código aquí...
          },
          (error: any) => {
            // Manejar cualquier error que ocurra al obtener el token
            console.error('Error al obtener el token de usuario:', error);
          }
        );
      }

      handleImageError(event: any) {
        console.error('Error al cargar la imagen:', event);
      }
      
      obtenerImagen(estudiante:Estudiante) {

        const headers = new HttpHeaders({
          'Authorization':`Bearer ${this.tokenSeleccionado}`
          // Agrega otros encabezados según sea necesario
        });
        // Hacer la solicitud GET para obtener la imagen
        this.http.get(API_URL_IMAGEN + estudiante.id + '.png', { headers:headers, responseType: 'arraybuffer' }).subscribe(
          (imagenData: ArrayBuffer) => {
            // Convertir los bytes en una URL de objeto
            const imagenBlob = new Blob([imagenData]);
            this.imagenUrl = URL.createObjectURL(imagenBlob);
          },
          error => {
            console.error('Error al cargar la imagen:', error);
          }
        );
      }
      
    

      
     //filtros


     aplicarFiltros(): void {
      this.estudiantesFiltrados = this.estudiantes.filter(estudiante =>
        Object.keys(this.filtros).every(key =>
          this.filtros[key] === null || estudiante[key] === this.filtros[key]
        )
      );
    
      if (this.estudiantesFiltrados.length === 0) {
        this.estudiantesFiltrados = this.estudiantes.slice();
      }
    }
    
     
   
    
     get filtrarEstudianes(): Estudiante[] {
      return this.filtroService.aplicarFiltros2(this.estudiantes, this.filtros);
    } 

// list-estudiantes.component.ts



    
    
    filtrarTabla(columna: string, valorSeleccionado: any) {
      // Filtrar los datos según la columna y el valor seleccionado
      this.estudiantesFiltrados = this.estudiantes.filter(estudiante => estudiante[columna] === valorSeleccionado);
    }
   
    

    //obtiene valores unicos de una columna
    getUniqueValues(column: string): string[] {//crea un nuevo arreglo solo con los valores de la columna 
      return this.estudiantesFiltrados.map(estudiante => estudiante[column])
      //crear arreglo que cumpla una condicion
      //value
      //index
      //self
      //commpara indices para ver si ya ocurrio la primera ocurrencia en un arreglo de los elementos
      //self.indexOf(value) devuelve el indice de la primera ocurrencia 
      // self.indexOf(value) === index compara indice de la primera ocurrencia y el indice actual ,si son iguales 
      //esto significa primera ocurrencia por lo que se conserva
      //y si son diferentes se descarta
                                       .filter((value, index, self) => self.indexOf(value) === index);
    }

   
  

    mostrarValoresEstudiantesFiltrado() :void{
      if (this.estudiantesFiltrados.length === 0) {
        console.log('El arreglo de estudiantes filtrados está vacío.');
      } else {
        this.estudiantesFiltrados.forEach(estudiante => {
          console.log(estudiante); // Imprime cada estudiante en la consola
        });
      }

     
    }
  // Método para mostrar las columnas y los estudiantes filtrados
  showFilteredData(): void {
    // Recorrer cada columna
    for (const column in this.estudiantesFiltrados[0]) {
      // Verificar si la columna es una propiedad propia del primer estudiante
      if (this.estudiantesFiltrados[0].hasOwnProperty(column)) {
        console.log(`Columna: ${column}`);
        // Recorrer cada estudiante filtrado
        for (const estudiante of this.estudiantesFiltrados) {
          console.log(`Estudiante - ${column}: ${estudiante[column]}`);
        }
      }
    }
}

cancelarEliminacion() {
  // Lógica para cancelar la eliminación del estudiante
  // Oculta la alerta de eliminación
  this.mostrarAlertaEliminar = false;
}

confirmarEliminacion(id: string) {
  this.estudianteAEliminar = id;
  this.mostrarAlertaEliminar = true;
}

eliminarEstudiante(id:any){
  
  if (this.estudianteAEliminar) {
this.estudiantesService.eliminiarEstudiantes(id).subscribe(


  response => {
    console.log('Estudiante Eliminado')
    this.obtenerEstudiantes();
    this.mostrarAlertaEliminar = false; // Ocultar la alerta después de eliminar

  },error=>{
    console.error('Error al eliminar la estudiatnte:', error);
  }
);}

}

editarEstudiante(estudiante:any){
  console.log('Editando estudiante:', estudiante);
    this.estudianteParaEditar = estudiante;
    this.mostrarFormularioEdicion = true; // Mostrar el formulario de edición


}

obtenerEstudiantes() {
  this.estudiantesService.getEstudiantes().subscribe(
    estudiantes => {
      this.estudiantes = estudiantes;
      this.estudiantesFiltrados = JSON.parse(JSON.stringify(this.estudiantes));
    },
    error => {
      console.error('Error al obtener la lista de estudiantes:', error);
    }
  );
}

guardarEstudiante(estudiante: any) {
  //sconsole.log(asistencia)
    this.estudiantesService.editarEstudiantes(estudiante).subscribe(
      response => {
        console.log('Asistencia actualizada:', response);
         // Actualiza los datos de asistencia
      
        this.estudianteParaEditar = null;
        this.mostrarFormularioEdicion = false; // Oculta el formulario después de guardar
        this.obtenerEstudiantes();
      },
      error => {
        console.error('Error al actualizar la asistencia:', error);
      }
    );
  }
  cancelarEdicion() {
    this.estudianteParaEditar = null;
    this.mostrarFormularioEdicion = false; // Oculta el formulario después de cancelar

  }


  limpiarFiltros(): void {
    this.filtros = {
      id: '',
      nombre: '',
      jornada: '',
      curso: ''
    }; // Resetea todos los filtros a sus valores iniciales
    this.estudiantesFiltrados = [...this.estudiantes]; // Restablece la lista filtrada a la lista completa
  }
  
  
  }

