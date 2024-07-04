import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { filterAsistencia } from 'src/app/modelos/filterAsistencia';
import { AsistenciaService } from 'src/app/services/asistencia.service';
@Component({
  selector: 'app-mensual',
  templateUrl: './mensual.component.html',
  styleUrls: ['./mensual.component.scss']
})
export class MensualComponent implements OnInit {

  

   
    
      
    
        asistencias: any[] = [];
        startDate: Date | undefined;
        endDate: Date | undefined;
        categorias:any[]=[];
        data: any[] = [];
        categoriasBarras:any[]=[];
        dataBarras: { [curso: string]: { [almuerzo: string]: number } } = {};
    
        dataLinea: { [fecha: string]: { [tipoComida: string]: number } } = {};
        categoriasLinea:string[]=[];
        // Luego, donde procesas los datos para dataBarras
        //this.dataBarras = asistenciasPorCursoYAlmuerzo;
           // myPieChart: Chart<keyof ChartTypeRegistry, (number | [number, number] | Point | BubbleDataPoint | null)[], unknown> | undefined;
    
    
        myPieChart: Chart<'pie'> | undefined;
        myBarChart: Chart<'bar'> | undefined;
        myLineChart: Chart<'line'> | undefined;
        constructor(private asistenciaService: AsistenciaService,private cdr: ChangeDetectorRef) {
          this.startDate = new Date();
          this.endDate = new Date();
        //  this.inicializarGraficos();
    
    
        }
        ngOnInit(): void {
        
          this.cambiarRangoFechas('mes');
    
    
        }
        cambiarRangoFechas(rango: string): void {
          const hoy = new Date();
          switch (rango) {
            case 'dia':
              this.startDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate(), 0, 0, 0); // Medianoche
              this.endDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate(), 23, 59, 59); // Final del día
              break;
            case 'semana':
              this.startDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate() - hoy.getDay());
              this.endDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate() + (6 - hoy.getDay()));
              break;
            case 'mes':
              this.startDate = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
              this.endDate = new Date(hoy.getFullYear(), hoy.getMonth() + 1, 0);
              break;
            default:
              break;
          }
          if (this.startDate && this.endDate) {
            this.startDate = new Date(this.startDate);
            this.endDate = new Date(this.endDate);
          } else {
            throw new Error('Fechas inválidas');
          }
             // Formatea las fechas a ISO
        const startDateISO = this.formatDateToISO(this.startDate);
        const endDateISO = this.formatDateToISO(this.endDate);
        this.cdr.detectChanges();
    
          this.obtenerAsistencias(); // Actualiza los datos según el nuevo rango de fechas
        }
          
        inicializarPastel(asistencias: any[]):void{
          const asistenciasPorAlmuerzo = asistencias.reduce((acumulador, asistencia) => {
            const almuerzoNombre = asistencia.almuerzo.nombre;
            acumulador[almuerzoNombre] = (acumulador[almuerzoNombre] || 0) + 1;
            return acumulador;
          }, {});
          this.categorias=Object.keys(asistenciasPorAlmuerzo);
          this.data = Object.values(asistenciasPorAlmuerzo);
        }
        inicializarBarras(asistencias: any[]): void {
          const asistenciasPorCursoYAlmuerzo = asistencias.reduce((acumulador, asistencia) => {
            const curso = asistencia.estudiante.curso;
            const almuerzoNombre = asistencia.almuerzo.nombre.charAt(0).toUpperCase() + asistencia.almuerzo.nombre.slice(1).toLowerCase(); // Asegura consistencia en nombres
            if (!acumulador[curso]) {
              acumulador[curso] = {
                'Almuerzo': 0,
                'Desayuno': 0,
                'Refrigerio': 0
              };
            }
            if (almuerzoNombre in acumulador[curso]) {
              acumulador[curso][almuerzoNombre]++;
            }
            return acumulador;
          }, {});
        
          // Convertir el objeto en el formato esperado
          const dataBarrasFormatted: { [curso: string]: { [almuerzo: string]: number } } = {};
          for (const curso in asistenciasPorCursoYAlmuerzo) {
            if (asistenciasPorCursoYAlmuerzo.hasOwnProperty(curso)) {
              dataBarrasFormatted[curso] = asistenciasPorCursoYAlmuerzo[curso];
            }
          }
        
          this.categoriasBarras = Object.keys(asistenciasPorCursoYAlmuerzo);
          this.dataBarras = dataBarrasFormatted;
        }// estadisticas.component.ts
    
        inicilizarLinea(asistencias: any[], startDate: Date, endDate: Date): void {
          const recuentoAsistencias: { [fecha: string]: { [tipoComida: string]: number } } = {};
        
          // Inicializar recuentoAsistencias con todas las fechas entre startDate y endDate
          const fechasCompletas = this.obtenerFechasCompletas(startDate, endDate);
          fechasCompletas.forEach(fecha => {
            recuentoAsistencias[fecha] = { Desayuno: 0, Refrigerio: 0, Almuerzo: 0 };
          });
        
          // Procesar los datos de asistencias
          asistencias.forEach(asistencia => {
            const fecha = new Date(asistencia.fecha).toDateString();
            const tipoComida = asistencia.almuerzo.nombre.charAt(0).toUpperCase() + asistencia.almuerzo.nombre.slice(1).toLowerCase();
        
            if (!recuentoAsistencias[fecha]) {
              recuentoAsistencias[fecha] = { Desayuno: 0, Refrigerio: 0, Almuerzo: 0 };
            }
        
            if (tipoComida in recuentoAsistencias[fecha]) {
              recuentoAsistencias[fecha][tipoComida]++;
            } else {
              console.error(`Tipo de comida desconocido: ${tipoComida}`);
            }
          });
        
          // Crear las categorías y los datos para el gráfico de línea
          this.categoriasLinea = fechasCompletas;
          this.dataLinea = recuentoAsistencias;
        }
        
        
        
    
        obtenerAsistencias(): void {
          if (this.startDate && this.endDate) {
            const filter = new filterAsistencia();
            filter.startDate = this.formatDateToISO(this.startDate);
            filter.endDate = this.formatDateToISO(this.endDate);
        
            console.log('Filter data:', filter); // Verifica los datos aquí
        
            this.asistenciaService.obtenerAsistencias(filter).subscribe(
              data => {
                this.asistencias = data;
                this.inicializarPastel(this.asistencias);
              this.inicializarBarras(this.asistencias);
                //this.generarPastel(this.asistencias);
              // this.generarGraficoBarras(this.asistencias);
               if (this.startDate && this.endDate) { // Verificar nuevamente
                  this.inicilizarLinea(this.asistencias, this.startDate, this.endDate);
              } else {
                console.error('Fechas no seleccionadas');
              }
              this.cdr.detectChanges();
    
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
    
       
     
    
        
        // estadisticas.component.ts
    
        
        
        
      obtenerFechasCompletas(startDate: Date, endDate: Date): string[] {
        // Crear un array de fechas entre startDate y endDate
        const fechasCompletas: string[] = [];
        let currentDate = new Date(startDate);
        while (currentDate <= endDate) {
          fechasCompletas.push(currentDate.toDateString());
          currentDate.setDate(currentDate.getDate() + 1); // Incrementar la fecha en 1 día
        }
        return fechasCompletas;
      }
        
        
        
      formatDateToISO(date: Date): string {
        return date.toISOString();
      }
      }
    
    
    
    
  
  
      
