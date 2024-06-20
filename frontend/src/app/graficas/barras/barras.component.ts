import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-barras',

  templateUrl: './barras.component.html',
  styleUrl: './barras.component.scss'
})
export class BarrasComponent implements OnInit {

  
    @Input() categories: string[] = [];
    @Input() data: { [curso: string]: { [almuerzo: string]: number } } = {};
  
    barrasChart: Chart<'bar'> | undefined;
  
    constructor() { }
  
    ngOnInit(): void {
      this.updateChart();
    }
  
    ngOnChanges(changes: SimpleChanges): void {
      if (changes['categories'] || changes['data']) {
        this.updateChart();
      }
    }
    updateChart(): void {
      const canvas = document.getElementById('barrasChart') as HTMLCanvasElement;
      if (canvas) {
        const ctx = canvas.getContext('2d');
        if (ctx) {
          if (this.barrasChart) {
            this.barrasChart.data.labels = this.categories;
            ['Desayuno', 'Refrigerio', 'Almuerzo'].forEach((almuerzoNombre, index) => {
              if (this.barrasChart) {
                const dataset = this.barrasChart.data.datasets[index];
              
              if (dataset) {
                dataset.data = this.categories.map(curso => this.data[curso]?.[almuerzoNombre] || 0);
              }
            }else{
              console.log("error barrachar undefined")
          }});
            this.barrasChart.update();
          } else {
            this.barrasChart = new Chart(ctx, {
              type: 'bar',
              data: {
                labels: this.categories,
                datasets: ['Almuerzo', 'Desayuno', 'Refrigerio'].map((almuerzoNombre, index) => {
                  return {
                    label: almuerzoNombre,
                    data: this.categories.map(curso => this.data[curso]?.[almuerzoNombre] || 0),
                    backgroundColor: [
                      'rgba(255, 99, 132, 0.2)',
                      'rgba(54, 162, 235, 0.2)',
                      'rgba(255, 206, 86, 0.2)'
                    ][index],
                    borderColor: [
                      'rgba(255, 99, 132, 1)',
                      'rgba(54, 162, 235, 1)',
                      'rgba(255, 206, 86, 1)'
                    ][index],
                    borderWidth: 1
                  };
                })
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
    }
  }
  

