import { Component, OnInit } from '@angular/core';
import { Estudiante } from 'src/app/modelos/estudiante';
import { EstudianteService } from 'src/app/services/estudiante.service';

@Component({
  selector: 'app-estudiantes',
  templateUrl: './estudiantes.component.html',
  styleUrls: ['./estudiantes.component.scss']
})
export class EstudiantesComponent implements OnInit {
  estudiantes: Estudiante[] = [];
  mostrarFormularioEstudiante: boolean = false;

  constructor(private estudianteService: EstudianteService) {}

  
  ngOnInit(): void {
    this.cargarEstudiantes();
  }

  cargarEstudiantes() {
    this.estudianteService.getEstudiantes().subscribe(
      data => {
        this.estudiantes = data;
      },
      error => {
        console.error('Error al cargar los estudiantes:', error);
      }
    );
  }

  abrirFormulario() {
    this.mostrarFormularioEstudiante = true;
  }

  cerrarFormulario() {
    this.mostrarFormularioEstudiante = false;
    this.cargarEstudiantes(); // Recargar la lista de estudiantes después de agregar uno nuevo
  }
}
