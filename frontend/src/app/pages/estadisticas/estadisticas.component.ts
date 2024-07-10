    import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
    import { Asistencia } from 'src/app/modelos/asistencia';
    import { AsistenciaService } from 'src/app/services/asistencia.service';
    import Chart from 'chart.js/auto'; // Import Chart.js using auto module
    import { BubbleDataPoint, ChartComponentLike, ChartConfiguration, ChartData, ChartOptions, ChartTypeRegistry, LinearScale, Point } from 'chart.js'; // Importa la escala lineal de Chart.js

    import { filterAsistencia } from 'src/app/modelos/filterAsistencia';
  import { AsistenciaDia } from 'src/app/modelos/asistenciaDia';
  import { Ventana } from 'src/app/modelos/ventana';
  import { EstudianteService } from 'src/app/services/estudiante.service';
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
      categorias:any[]= ['Desayuno', 'Almuerzo', 'Refrigerio'];
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


      totalEstudiantes: number = 0; // Propiedad para almacenar el total de estudiantes
      totalEstudiantesPeriodo: number = 0; // Total de estudiantes ajustado por el periodo

      selectedCategory: string = 'desayuno'; // Inicializar con una categoría por defecto
      categoriasDisponibles: string[] =  ['Desayuno', 'Almuerzo', 'Refrigerio']; // Asegurar que esta propiedad esté definida y sea inicializada



      constructor(private estudianteService:EstudianteService,  private asistenciaService: AsistenciaService,private cdr: ChangeDetectorRef) {
        this.startDate = new Date();
        this.endDate = new Date();
      //  this.inicializarGraficos();
      this.categorias = [this.selectedCategory, 'No asistidos'];


      }
      ngOnInit(): void {
        this.estudianteService.obtenerTotalEstudiantes().subscribe(
          (total: number) => { // Especificar el tipo de 'total' como number
            this.totalEstudiantes = total;
            console.log('Total de estudiantes:', this.totalEstudiantes);
            this.categoriasDisponibles = ['Desayuno', 'Almuerzo', 'Refrigerio']; // Ajusta esto según tu lógica
            this.cambiarRangoFechas('dia');

            this.obtenerAsistencias(); // Llamar a obtenerAsistencias después de obtener el total de estudiantes
        //    this.actualizarCategoriasDisponibles(this.asistencias);
          },
          error => {
            console.error('Error al obtener el número total de estudiantes', error);
          }
        );

        

      }
      cambiarRangoFechas(rango: string): void {
        const hoy = new Date();
        let diasEnPeriodo = 1; // Por defecto 1 día

        switch (rango) {
          case 'dia':
            this.startDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate(), 0, 0, 0); // Medianoche
            this.endDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate(), 23, 59, 59); // Final del día
            diasEnPeriodo = 1;

            break;
          case 'semana':
            this.startDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate() - hoy.getDay());
            this.endDate = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate() + (6 - hoy.getDay()));
            diasEnPeriodo = 7;

            break;
          case 'mes':
            this.startDate = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
            this.endDate = new Date(hoy.getFullYear(), hoy.getMonth() + 1, 0);
            diasEnPeriodo = new Date(hoy.getFullYear(), hoy.getMonth() + 1, 0).getDate();

            break;
          default:
            break;
        }

        this.totalEstudiantesPeriodo = this.totalEstudiantes * diasEnPeriodo;


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
        
    
    inicializarPastel(asistencias: any[]): void {
      //reduce los los valores de una array a un valor 
      const asistenciasPorCategoria = asistencias.reduce((acumulador, asistencia) => {
        const categoriaNombre = asistencia.almuerzo.nombre;
        acumulador[categoriaNombre] = (acumulador[categoriaNombre] || 0) + 1;
        return acumulador;
      }, {});
  // Calcular estudiantes no asistidos en la categoría seleccionada
  const estudiantesAsistidosEnCategoria = asistenciasPorCategoria[this.selectedCategory] || 0;
  const estudiantesSinAsistenciaEnCategoria = this.totalEstudiantesPeriodo - estudiantesAsistidosEnCategoria;

  this.data = [estudiantesAsistidosEnCategoria, estudiantesSinAsistenciaEnCategoria];
  this.categorias = [this.selectedCategory, 'No asistido'];
}
  
    onCategorySelectionChange(event: Event): void {
      const selectElement = event.target as HTMLSelectElement;
      this.selectedCategory = selectElement.value;
      this.obtenerAsistencias();
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

            this.actualizarCategoriasDisponibles(this.asistencias);


              this.inicializarPastel(this.asistencias);
            this.inicializarBarras(this.asistencias);
          



            
            if (this.startDate && this.endDate) { // Verificar nuevamente
                this.inicilizarLinea(this.asistencias, this.startDate, this.endDate);
            } else {
              console.error('Fechas no seleccionadas');
            }

            // Calcular estudiantes sin asistencia
      
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
      
      actualizarCategoriasDisponibles(asistencias: any[]): void {
        // Obtener todas las categorías únicas de las asistencias
        const categoriasUnicas = Array.from(new Set(asistencias.map(asistencia => asistencia.almuerzo.nombre)));
      
        // Ordenar alfabéticamente o según tus criterios
        this.categoriasDisponibles = categoriasUnicas.sort();
      }
     
      seleccionarCategoria(categoria: string): void {
        this.selectedCategory = categoria;
      this.obtenerAsistencias();}
  

      
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
