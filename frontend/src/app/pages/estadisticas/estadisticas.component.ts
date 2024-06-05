  import { Component, OnInit } from '@angular/core';
  import { Asistencia } from 'src/app/modelos/asistencia';
  import { AsistenciaService } from 'src/app/services/asistencia.service';
  import Chart from 'chart.js/auto'; // Import Chart.js using auto module
  import { LinearScale } from 'chart.js'; // Importa la escala lineal de Chart.js

  import { filterAsistencia } from 'src/app/modelos/filterAsistencia';
import { AsistenciaDia } from 'src/app/modelos/asistenciaDia';
import { Ventana } from 'src/app/modelos/ventana';
declare let window: Ventana;
  

@Component({
    selector: 'app-estadisticas',
  
    templateUrl: './estadisticas.component.html',
    styleUrl: './estadisticas.component.scss'
  })
  
  export class EstadisticasComponent implements OnInit {
    asistencias: any[] = [];
    startDate: Date | undefined;
    endDate: Date | undefined;

    constructor(private asistenciaService: AsistenciaService) {}

    ngOnInit(): void {}

    obtenerAsistencias(): void {
      if (this.startDate && this.endDate) {
        const filter = new filterAsistencia();
        filter.startDate = this.formatDateToISO(this.startDate);
        filter.endDate = this.formatDateToISO(this.endDate);
    
        console.log('Filter data:', filter); // Verifica los datos aquí
    
        this.asistenciaService.obtenerAsistencias(filter).subscribe(
          data => {
            this.asistencias = data;
           this.generarGraficoPastel(this.asistencias);
           this.generarGraficoBarras(this.asistencias);
          },
          error => {
            console.error('Error al obtener asistencias', error);
          }
        );

      //  this.generarGraficoPastel(this.asistencias);
      } else {
        console.error('Fechas no seleccionadas');
      }
    }
    
    
    // estadisticas.component.ts (continuación)

    generarGraficoPastel(asistencias: any[]): void {
      // Procesamiento de datos: Obtener el recuento de asistencias por almuerzo
      const asistenciasPorAlmuerzo = asistencias.reduce((acumulador, asistencia) => {
        const almuerzoNombre = asistencia.almuerzo.nombre;
        acumulador[almuerzoNombre] = (acumulador[almuerzoNombre] || 0) + 1;
        return acumulador;
      }, {});
    
      // Configuración del gráfico
      const canvas = document.getElementById('myChart') as HTMLCanvasElement;
      if (canvas instanceof HTMLCanvasElement) {
        const ctx = canvas.getContext('2d');
        if (ctx) {
          // Establecer el color de fondo del canvas
          if (window.myPieChart) {
            window.myPieChart.destroy();
          }
          ctx.fillStyle = 'white';
          ctx.fillRect(0, 0, canvas.width, canvas.height);
    
          // Crear el gráfico de pastel
          const myPieChart = new Chart(ctx, {
            type: 'pie',
            data: {
              labels: Object.keys(asistenciasPorAlmuerzo),
              datasets: [{
                label: 'Asistencias por almuerzo',
                data: Object.values(asistenciasPorAlmuerzo),
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
              responsive: true,
              plugins: {
                tooltip: {
                  callbacks: {
                    label: (context) => {
                      const label = context.label || '';
                      const value = context.raw || '';
                      return label + ': ' + value;
                    }
                  }
                }
              }
            }
          });
        }
      }
    }
    
    // estadisticas.component.ts

    generarGraficoBarras(asistencias: any[]): void {
      // Procesamiento de datos: Obtener el recuento de asistencias por curso y tipo de almuerzo
      const asistenciasPorCursoYAlmuerzo = asistencias.reduce((acumulador, asistencia) => {
        const curso = asistencia.estudiante.curso;
        const almuerzoNombre = asistencia.almuerzo.nombre;
        if (!acumulador[curso]) {
          acumulador[curso] = {
            'Almuerzo': 0,
            'Desayuno': 0,
            'Refigerio': 0
          };
        }
        acumulador[curso][almuerzoNombre]++;
        return acumulador;
      }, {});
    
      // Configuración del gráfico de barras
      const canvas = document.getElementById('barrasChart') as HTMLCanvasElement;
      if (canvas instanceof HTMLCanvasElement) {
        const ctx = canvas.getContext('2d');
        if (ctx) {
          // Establecer el color de fondo del canvas
          ctx.fillStyle = 'white';
          ctx.fillRect(0, 0, canvas.width, canvas.height);
    
          // Crear el gráfico de barras
          const cursos = Object.keys(asistenciasPorCursoYAlmuerzo);
          const datasets = Object.keys(asistenciasPorCursoYAlmuerzo[cursos[0]]).map((almuerzoNombre, index) => {
            return {
              label: almuerzoNombre,
              data: cursos.map(curso => asistenciasPorCursoYAlmuerzo[curso][almuerzoNombre] || 0), // Si no hay asistencias, establecer a 0
              backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)',
                'rgba(153, 102, 255, 0.2)'
              ][index],
              borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 206, 86, 1)',
                'rgba(75, 192, 192, 1)',
                'rgba(153, 102, 255, 1)'
              ][index],
              borderWidth: 1
            };
          });
    
          // Crear la gráfica de barras
          new Chart(ctx, {
            type: 'bar',
            data: {
              labels: cursos,
              datasets: datasets
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
    }
    

  formatDateToISO(date: Date): string {
    return date.toISOString();
  }
  }
