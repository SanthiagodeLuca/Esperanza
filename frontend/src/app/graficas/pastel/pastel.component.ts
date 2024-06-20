import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-pastel',
  templateUrl: './pastel.component.html',
  styleUrls: ['./pastel.component.scss']
})
export class PastelComponent implements OnInit {
  @Input() categories: string[] = [];
  @Input() data: number[] = [];

  pieChart: Chart<'pie'> | undefined;

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
          type: 'pie',
          data: {
            labels: this.categories,
            datasets: [{
              data: this.data,
              backgroundColor: [
                'rgba(255, 99, 132, 0.2)', // Rojo
                'rgba(255, 206, 86, 0.2)',  // Amarillo
                'rgba(54, 162, 235, 0.2)',  // Azul
              ],
              borderColor: [
                'rgba(255, 99, 132, 1)', // Rojo
                'rgba(255, 206, 86, 1)',  // Amarillo
                'rgba(54, 162, 235, 1)',  // Azul
              ],
              borderWidth: 1
            }]
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
