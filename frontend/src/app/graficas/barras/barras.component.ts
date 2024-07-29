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
                      'rgba(0, 144, 71, 0.9)',
                      'rgba(205, 113, 0, 0.9)',
                      'rgba(180, 0, 64, 0.9)'
                    ][index],
                    borderColor: [
                      'rgba(0, 144, 71, 1)',
                      'rgba(205, 113, 0, 1)',
                      'rgba(180, 0, 64, 1)'
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
  

