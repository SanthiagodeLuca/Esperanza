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
    filtros: { [key: string]: any } = {};

    constructor(private estudiantesService:EstudianteService,
      private imagenService:ImagenService,private filtroService:FiltroService,private loginService:LoginService
    ,private http:HttpClient,private userService:UserService,private tokenInformacionService:TokenInformacionService){}
    data=this.loginService.currentUserData;
    //metodo cunado se inica el componente
    ngOnInit(): void {

      
      //se llama al metodo de estudiante sevice 
      //se subcribe(observador esta atengo a cualquier datos que envie el observable) al observable 
      //se rellena a los estudiantes(datos) el array con los datos que se tienen  
      //internamente en subcribe cuando se hace un callback el pasa el valor como paremetro de la funcion callback en este caso
      //data pasa a tener el valor del array estudiantes y aqui solo lo inicializamoss
      this.estudiantesService.getEstudiantes().pipe(take(1)).subscribe(data => {
     
        // OPCION  EJECUTAR UNA VEZ this.estudiantesService.getEstudiantes().pipe(take(1)).subscribe(data => {
        //.subcribe se subscribe al observable 

        //aqui en http subcribe ya recibio los datos 
        //lo que haces con el data.map es cambiar los datos que ya tienes y agregar un nuevo elemento
        //... indica que copia todas las propiedades del objeto original ademas se agregan propiedades
       
        this.estudiantes = data.map(estudiante=>({...estudiante,imagenVisible:false,imagenUrl:API_URL_IMAGEN+estudiante.id+'.png'}));
        // como es asincronico las demas lineas de codigo se ejecutan mientras se espera toda la respuesta 
        // debido a que la respuesta aun no esta lista no guarda nada 

        this.estudiantesFiltrados = JSON.parse(JSON.stringify(this.estudiantes));

      });
     // this.estudiantesFiltrados = JSON.parse(JSON.stringify(this.estudiantes));
     //this.estudiantesFiltrados = this.estudiantes.slice();

     this.cargarImagen();
     this.obtenerAuthoritiesUser();
    
    // this.obtenerImagen();
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
       // const urlImagen = `http://localhost:8085/api/images/${estudiante.id}`; // Ejemplo de URL al servidor
      
     
        // Realiza la solicitud HTTP para obtener la imagen
        /*this.http.get(urlImagen, {
          headers: {
            Authorization: `Bearer ${token}`
          },
          responseType: 'blob'
        }).subscribe(
          (imagenBlob: Blob) => {
            // Convierte el blob en una URL de objeto
            const imagenUrl = URL.createObjectURL(imagenBlob);
            // Actualiza la URL de la imagen en el objeto estudiante
            estudiante.imagenUrl = imagenUrl;
          },
          error => {
            console.error('Error al cargar la imagen:', error);
          }
        );
        */
      
      
      







      
     //filtros


     aplicarFiltros(): void {
      // Vuevlve a reconvertir el estudiantes filtro 
     
       this.estudiantesFiltrados = this.estudiantes.filter(
        estudiante => {
        // Object.keys ejemplo 
      
        
        // cada clave cumple con una condicion 
        //object.key devuelve un arreglo con las llaves 
         Object.keys(this.filtros)
        // every metodo que verificasi lo elemento cumplen una condicion 
         .every(
          key => {
          // Aquí se muestra la clave actual en cada ciclo
         // console.log('Ciclo de Object.keys:', key);
    
          // Esta es la lógica de cada iteración en el método every
          //filtros[key] el valor que tiene ese filtro
          var valor=this.filtros[key];
          var valorEstudiante =estudiante[key];
          const condition = !this.filtros[key] || estudiante[key] === this.filtros[key];
    
          // Se muestra el resultado de la condición en cada iteración
          //console.log('Condición en cada iteración de every:', condition);
    
          // Se retorna el resultado de la condición para cada clave
          return condition;
        });
      });

      if(this.estudiantesFiltrados.length===0){
        this.estudiantesFiltrados= this.estudiantes.slice();
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

  }

