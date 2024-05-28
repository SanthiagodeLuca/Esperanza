import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { Observable, of, Subscription } from 'rxjs';
import { Horario } from 'src/app/modelos/horario';
import { HorarioService } from 'src/app/services/hour/horario.service';


@Component({
  selector: 'app-almuerzo',
  templateUrl: './almuerzo.component.html',
  styleUrls: ['./almuerzo.component.scss']
})
export class AlmuerzoComponent implements OnDestroy, OnInit {

  public startHour: number | null = null;
  public startMinute: number | null = null;
  public endHour: number | null = null;
  public endMinute: number | null = null;
  
  tokenSeleccionado: string = "";

  filteredAlmuerzoHours!: Observable<number[]>;
  filteredAlmuerzoMinutes!: Observable<number[]>;

  almuerzoDataSubscription!: Subscription;
  almuerzoData: Horario = {
    id: null,
    startHour: null,
    startMinute: null,
    endHour: null,
    endMinute: null,
    tipoComida: null
  }; // Inicializar con un objeto vacío para evitar errores

  constructor(
    private horarioService: HorarioService,
    private cdr: ChangeDetectorRef,
   
  ) {}

  onUpdate() {
    console.log("hola");
    // console.log(this.startHour, this.startMinute, this.endHour, this.endMinute)
  }

  ngOnInit(): void {
    this.almuerzoData = {
      id: null,
      startHour: null,
      startMinute: null,
      endHour: null,
      endMinute: null,
      tipoComida: null
    };
    this.initializeAlmuerzoTimes();
  }

  enviarDatosAlmuerzo(): void {
    // Aquí envías los datos del almuerzo al servicio HorarioService
    if (this.startHour !== null && this.startMinute !== null && this.endHour !== null && this.endMinute !== null) {
      this.almuerzoData.id = 3;
      // Asigna los valores a las propiedades de almuerzoData
      this.almuerzoData.startHour = this.startHour;
      this.almuerzoData.startMinute = this.startMinute;
      this.almuerzoData.endHour = this.endHour;
      this.almuerzoData.endMinute = this.endMinute;
      this.almuerzoData.tipoComida = "almuerzo";
  
      // Luego, llama al método actualizarAlmuerzo del servicio HorarioService
      this.horarioService.actualizarComida(this.almuerzoData);
      this.horarioService.enviarComida(this.almuerzoData,this.almuerzoData.id);
      this.cdr.detectChanges();
    } else {
      console.log("Por favor, seleccione todos los valores antes de enviar.");
    }
  }

  private initializeAlmuerzoTimes(): void {
    const hours = Array.from({ length: 24 }, (v, k) => k); // [0, 1, 2, ..., 23]
    const minutes = Array.from({ length: 60 }, (v, k) => k); // [0, 1, 2, ..., 59]
    this.filteredAlmuerzoHours = of(hours);
    this.filteredAlmuerzoMinutes = of(minutes);
  }

  ngOnDestroy() {
    if (this.almuerzoDataSubscription) {
      this.almuerzoDataSubscription.unsubscribe();
    }
  }
}
