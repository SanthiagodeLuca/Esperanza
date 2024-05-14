import { Component } from '@angular/core';

@Component({
  selector: 'app-horario',
 
  templateUrl: './horario.component.html',
  styleUrl: './horario.component.scss'
})
export class HorarioComponent {
  hours = Array.from({ length: 24 }, (_, i) => i);
  minutes = Array.from({ length: 60 }, (_, i) => i);
}
