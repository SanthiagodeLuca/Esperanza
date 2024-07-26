import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Horario } from 'src/app/modelos/horario';
import { HorarioService } from 'src/app/services/hour/horario.service';

@Component({
  selector: 'app-daily-schedule',
  templateUrl: './daily-schedule.component.html',
  styleUrls: ['./daily-schedule.component.scss']
})
export class DailyScheduleComponent implements OnInit, OnDestroy {
  breakfast: Horario | null = null;
  lunch: Horario | null = null;
  snack: Horario | null = null;
  currentDate = new Date().toLocaleDateString();
  hours: string[] = ['5 am', '6 am', '7 am', '8 am', '9 am', '10 am', '11 am', '12 pm', '1 pm', '2 pm', '3 pm', '4 pm', '5 pm'];

  private subscriptions: Subscription = new Subscription();

  constructor(private horarioService: HorarioService) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.horarioService.getSnackSchedule(1).subscribe(data => this.snack = data)
    );
    this.subscriptions.add(
      this.horarioService.getBreakfastSchedule(2).subscribe(data => this.breakfast = data)
    );
    this.subscriptions.add(
      this.horarioService.getLunchSchedule(3).subscribe(data => this.lunch = data)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe(); 
  }

  getSchedulePosition(schedule: Horario) {
    // Ajustar las horas de inicio y fin para que estén en el rango del contenedor
    const adjustedStartHour = Math.max(schedule.startHour ?? 0, 5); // No menor a 5 AM
    const adjustedEndHour = Math.min(schedule.endHour ?? 0, 17); // No mayor a 5 PM (17 en formato 24h)
    
    // Convertir horas a píxeles desde las 5 AM
    const startPosition = Math.max((adjustedStartHour - 5) * 40 + (schedule.startMinute ?? 0), 0);
    const endPosition = Math.min((adjustedEndHour - 5) * 40 + (schedule.endMinute ?? 0), 720); // Limitar al máximo de 720px
    
    // Asegurarse de que la altura no sea negativa
    const height = Math.max(endPosition - startPosition, 0);
    
    return {
      top: `${startPosition}px`,
      height: `${height}px`
    };
  }
  
  
  
  
}
