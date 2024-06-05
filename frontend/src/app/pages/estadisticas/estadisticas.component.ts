  import { Component, OnInit } from '@angular/core';
  import { Asistencia } from 'src/app/modelos/asistencia';
  import { AsistenciaService } from 'src/app/services/asistencia.service';
  import { Chart } from 'chart.js';
  import { filterAsistencia } from 'src/app/modelos/filterAsistencia';
  @Component({
    selector: 'app-estadisticas',
  
    templateUrl: './estadisticas.component.html',
    styleUrl: './estadisticas.component.scss'
  })
  export class EstadisticasComponent implements OnInit {
    asistencias: Asistencia[] = [];
    startDate: Date | undefined;
    endDate: Date | undefined;
   

    constructor(private asistenciaService: AsistenciaService) {}

    ngOnInit(): void {}

    obtenerAsistencias(): void {
     

      const filter = new filterAsistencia();
        filter.startDate = this.startDate;
        filter.endDate = this.endDate;
     
      this.asistenciaService.obtenerAsistencias(filter).subscribe(
        data => {
          this.asistencias = data;
          this.generarEstadisticas();
        },
        error => {
          console.error('Error al obtener asistencias', error);
        }
      );
    }

    
    // estadisticas.component.ts (continuación)


  generarEstadisticas(): void {
    const ctx = document.getElementById('myChart') as HTMLCanvasElement;
    const myChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes'],
        datasets: [{
          label: '# de Asistencias',
          data: [12, 19, 3, 5, 2], // Reemplaza estos datos con los tuyos procesados
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)'
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)'
          ],
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  }
