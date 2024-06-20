import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { Asistencia } from 'src/app/modelos/asistencia';
import { AsistenciaNueva } from 'src/app/modelos/asistenciaNueva';

@Component({
  selector: 'app-editar',
 
  templateUrl: './editar.component.html',
  styleUrl: './editar.component.scss'
})
export class EditarComponent implements OnChanges  {
  @Input() asistencia: AsistenciaNueva | null = null;
  @Output() onSave = new EventEmitter<Asistencia>();
  @Output() onCancel = new EventEmitter<void>();


  ngOnChanges(changes: SimpleChanges): void {
    // Verificar si la propiedad 'asistencia' cambió
    if (changes['asistencia']) {
      console.log('Asistencia cambió:', this.asistencia);
    }
  }
  save() {
    if (this.asistencia) {
      const pad = (num: number): string => num.toString().padStart(2, '0');
      
      const formatFecha = (fecha: Date): string => {
        return `${fecha.getFullYear()}-${pad(fecha.getMonth() + 1)}-${pad(fecha.getDate())} ${pad(fecha.getHours())}:${pad(fecha.getMinutes())}:${pad(fecha.getSeconds())}`;
      };
  
      const updatedAsistencia: any = {
        id: this.asistencia.id,
        estudiante: +this.asistencia.estudiante.id, // Convertir a número usando el operador +
        almuerzo: +this.asistencia.almuerzo.id, // Convertir a número usando el operador +
        fecha: formatFecha(new Date(this.asistencia.fecha)) // Formatear la fecha
      };
  
      this.onSave.emit(updatedAsistencia);
    }
  }
  

  cancel() {
    this.onCancel.emit();
  }
  obtenerFechaFormateada(): string {
    if (this.asistencia && this.asistencia.fecha) {
      const fecha = new Date(this.asistencia.fecha);
      return fecha.toISOString().slice(0, 10); // Formato "yyyy-MM-dd"
    }
    return '';
  }


}

