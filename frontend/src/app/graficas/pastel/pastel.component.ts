import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-pastel',
  templateUrl: './pastel.component.html',
  styleUrls: ['./pastel.component.scss']
})
export class PastelComponent implements OnInit {
  @Input() categories: string[] = ['Registrados', 'No Registrados']; // Etiquetas actualizadas
  @Input() data: number[] = [0, 0]; // Datos por defecto

  //selectedCategory: string = 'Desayuno';

  pieChart: Chart<'doughnut'> | undefined;

  constructor() { }

  ngOnInit(): void {
    this.generatePieChart();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['categories'] || changes['data']) {
      this.updateChart();
    }
  }
  generatePieChart(): void {
    const canvas = document.getElementById('pieChart') as HTMLCanvasElement;
    if (canvas) {
      const ctx = canvas.getContext('2d');
      if (ctx) {
        this.pieChart = new Chart(ctx, {
          type: 'doughnut',
          data: {
            labels: this.categories,
            datasets: [{
              data: this.data,
              backgroundColor: [
                'rgba(17, 174, 94, 1)', // Verde
                'rgba(205, 49, 0, 1)',  // Rojo
              ],
              borderColor: [
                'rgba(17, 174, 94, 1)', // Verde
                'rgba(205, 49, 0, 1)',  // Rojo
              ],
              borderWidth: 1
            }]
          },

          options: {
            cutout: '70%', // Tamaño del agujero en el centro del gráfico
          }
        });
      }
    }
  }

 
  updateChart(): void {
    if (this.pieChart) {
      this.pieChart.data.labels = this.categories;
      this.pieChart.data.datasets[0].data = this.data;
      this.pieChart.update();
    } else {
      this.generatePieChart();
    }
  }
}
