import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-linea',
  templateUrl: './linea.component.html',
  styleUrls: ['./linea.component.scss']
})
export class LineaComponent implements OnInit {

  @Input() categorias: string[] = [];
  @Input() data: { [fecha: string]: { [tipoComida: string]: number } } = {};

  lineaChart: Chart<'line'> | undefined;
  selectedComida: string = 'Desayuno'; // Tipo de comida seleccionado

  constructor() { }

  ngOnInit(): void {
    this.updateChart();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['categorias'] || changes['data']) {
      this.updateChart();
    }
  }
 // Método para actualizar o crear un gráfico de línea
updateChart(): void {
  // Obtener el elemento canvas del DOM donde se renderizará el gráfico
  const canvas = document.getElementById('lineChart') as HTMLCanvasElement;
  if (canvas) {
    // Obtener el contexto 2D del canvas
    const ctx = canvas.getContext('2d');
    if (ctx) {
      // Verificar si el gráfico ya existe
      if (this.lineaChart) {
        // Si el gráfico ya existe, actualizar los datos y etiquetas del gráfico existente
        this.lineaChart.data.labels = this.categorias; // Actualizar las etiquetas del eje X con las categorías
        this.lineaChart.data.datasets.forEach((dataset) => {
          // Obtener el tipo de comida del dataset actual (Desayuno, Refrigerio, Almuerzo)
          const tipoComida = dataset.label;
          if (tipoComida) {
            // Actualizar los datos del dataset con los valores correspondientes de 'data'
            dataset.data = this.categorias.map(fecha => this.data[fecha]?.[tipoComida] || 0);
          }
        });
        // Llamar al método update() para aplicar los cambios al gráfico
        this.lineaChart.update();
      } else {
        // Si el gráfico no existe, crear un nuevo gráfico
        this.lineaChart = new Chart(ctx, {
          type: 'line', // Especificar el tipo de gráfico como línea
          data: {
            labels: this.categorias, // Establecer las etiquetas del eje X
            datasets: ['Desayuno', 'Refrigerio', 'Almuerzo'].map((tipoComida, index) => ({
              label: tipoComida, // Etiqueta del dataset (Desayuno, Refrigerio, Almuerzo)
              data: this.categorias.map(fecha => this.data[fecha]?.[tipoComida] || 0), // Datos del dataset
              fill: false, // No llenar el área bajo la línea
              borderColor: [
                'rgba(54, 162, 235, 1)', // Rojo para Desayuno 
                'rgba(255, 206, 86, 1)', //Azul para Refrigerio

                'rgba(255, 99, 132, 1)', // amarillo 
              ][index], // Color de la línea
              borderWidth: 1, // Ancho del borde de la línea
              pointRadius: 4, // Radio de los puntos en la línea
              pointHoverRadius: 6, // Radio de los puntos al pasar el mouse sobre ellos
            }))
          },
          options: {
            scales: {
              y: {
                beginAtZero: true // El eje Y comienza en cero
              }
            }
          }
        });
      }
    }
  }
}


  selectComida(comida: string): void {
    this.selectedComida = comida;
    this.updateChart();
  }
}
