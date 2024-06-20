import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { Estudiante } from 'src/app/modelos/estudiante';

@Component({
  selector: 'app-formulario-estudiante',
  templateUrl: './formulario-estudiante.component.html',
  styleUrls: ['./formulario-estudiante.component.scss']
})
export class FormularioEstudianteComponent implements OnChanges{
  @Input() estudiante: Estudiante | null = null;
  @Output() onSave = new EventEmitter<Estudiante>();
  @Output() onCancel = new EventEmitter<void>();


  ngOnChanges(changes: SimpleChanges): void {
    // Verificar si la propiedad 'asistencia' cambió
    if (changes['estudiante']) {
      console.log('Estudiante cambió:', this.estudiante);
    }
  }
  save() {
    if (this.estudiante) {
      const updatedEstudiante: any = {
        nombre: this.estudiante.nombre,
        jornada: this.estudiante.jornada, // Convertir a número usando el operador +
        curso: this.estudiante.curso, // Convertir a número usando el operador +
      //  imagen:+this.estudiante.imagen,
        especial:this.estudiante.especial
        
      };
   console.log(updatedEstudiante)
      this.onSave.emit(updatedEstudiante);
    }
  }

  cancelarEdicion() {
    this.onCancel.emit();
  }
}
