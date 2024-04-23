  import { Component, OnInit } from '@angular/core'; // Importa Component y OnInit de Angular Core

  import { Criterio } from '../modelos/criterio'; // Importa el modelo de Criterio
  import { CriterioService } from '../services/criterios.service'; // Importa el servicio CriterioService
  import { NgForm } from '@angular/forms';

  @Component({
    selector: 'app-criterio', // Selector del componente
    templateUrl: './criterio.component.html', // Plantilla HTML asociada al componente
    styleUrls: ['./criterio.component.scss'] // Estilos CSS asociados al componente
  })
  export class CriteriosComponent implements OnInit { // Clase del componente que implementa OnInit
    criterios: Criterio[] = []; // Array para almacenar los criterios
    criterioSeleccionado: Criterio = { id: undefined, nombre: '', tipoCriterio: 'Longitud de Estudiantes' }; 

  // Almacena el criterio seleccionado para edición
    nuevoCriterio: Criterio = { id: 0, nombre: '' ,tipoCriterio:''}; // Inicializa el id como undefined
  

    constructor(private criterioService: CriterioService) { } // Constructor del componente, inyecta CriterioService

    ngOnInit(): void { // Método que se ejecuta cuando se inicializa el componente
      
      //this.criterioService.obtenerCriterios
      this.obtenerCriterios(); // Cuando se inicializa el componente, obtiene los criterios
    }

    // Método para obtener los criterios desde el servicio
    obtenerCriterios(): void {
      this.criterioService.obtenerCriterios() // Llama al método del servicio para obtener los criterios
        .subscribe((criterios: Criterio[]) => this.criterios = criterios); // Actualiza el array de criterios
    }

    // Método para editar un criterio seleccionado
    editarCriterio(criterio: Criterio): void {
      this.criterioSeleccionado = { ...criterio }; // Copia el criterio seleccionado para la edición
    }

    // Método para guardar los cambios realizados en un criterio
    guardarCambios(): void {
      if (this.criterioSeleccionado) { // Verifica si hay un criterio seleccionado
        this.criterioService.actualizarCriterio(this.criterioSeleccionado) // Llama al método del servicio para actualizar el criterio
          .subscribe(() => {
            this.obtenerCriterios();  // Vuelve a obtener los criterios después de la actualización
          // this.criterioSeleccionado = undefined; // Limpia el criterio seleccionado
          });
      }
    }
    agregarCriterio(formulario: NgForm): void {
      // Verifica si el formulario es válido
      if (formulario.valid) {
       
       this.nuevoCriterio.id=formulario.value.id;
        this.nuevoCriterio.nombre = formulario.value.nombre;
        this.nuevoCriterio.tipoCriterio=formulario.value.tipoCriterio;
        // Llama al servicio para agregar un nuevo criterio, pasando el nuevoCriterio
        // como argumento. El método agregarCriterio del servicio probablemente hará una
        // solicitud HTTP para agregar el criterio al backend.
        this.criterioService.agregarCriterio(this.nuevoCriterio)
          // Suscribe una función al observable devuelto por el servicio. Esta función se
          // ejecutará cuando se complete la operación de agregar criterio.
          .subscribe((criterio: Criterio) => {
            // La función recibe el criterio recién agregado como parámetro. Aquí se imprime
            // un mensaje en la consola con la información del criterio agregado.
            console.log('Criterio agregado:', criterio);
            // Una vez que se ha agregado el criterio con éxito, se restablece el formulario.
            formulario.resetForm();
          });
      }
    }


  }
