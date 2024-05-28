import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { Horario } from 'src/app/modelos/horario';
import { JwtInterceptorService } from 'src/app/services/auth/jwt-interceptor.service';
import { HorarioService } from 'src/app/services/hour/horario.service';

@Component({
  selector: 'app-horario',
  templateUrl: './horario.component.html',
  styleUrls: ['./horario.component.scss']
})
export class HorarioComponent implements OnInit, OnDestroy {
  refrigerioDataSubscription!: Subscription;
  refrigerioData: Horario = {
    id:null,
    startHour: null,
    startMinute: null,
    endHour: null,
    endMinute: null,
    tipoComida:null

  };

  constructor(private horarioService: HorarioService, private cdr: ChangeDetectorRef) {}

  ngOnDestroy(): void {
    if (this.refrigerioDataSubscription) {
      this.refrigerioDataSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.refrigerioDataSubscription = this.horarioService.refrigerioData$.subscribe((data: Horario) => {
      if (data) {
        this.refrigerioData = data;
        this.cdr.detectChanges();
        console.log("Datos recibidos:", data);
        console.log("refrigerioData completo:", this.refrigerioData);
        console.log("startHour:", this.refrigerioData?.startHour);
      } else {
        console.log("Datos es undefined");
      }
    });
    console.log("Fin de ngOnInit");
  }
}  
