import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Estudiante } from 'src/app/modelos/estudiante';
import { EstudianteService } from 'src/app/services/estudiante.service';

@Component({
  selector: 'app-formulario-crear-estudiante',
  templateUrl: './formulario-crear-estudiante.component.html',
  styleUrls: ['./formulario-crear-estudiante.component.scss']
})
export class FormularioCrearEstudianteComponent {
  
  @Input() mostrarFormulario: boolean = false;
  @Output() onClose = new EventEmitter<void>();

  estudiante: Estudiante = {
    id: '',
    nombre: '',
    jornada: '',
    curso: '',
    especial: false,
    imagen: null,
    asistencias: [],
    imagenVisible: false,
    imagenUrl: ''
  };

  constructor(private estudianteService: EstudianteService) {}

  guardarEstudiante() {
    this.estudianteService.agregarEstudiante(this.estudiante).subscribe(
      response => {
        console.log('Estudiante guardado:', response);
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
