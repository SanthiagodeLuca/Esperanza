import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { Observable, of, Subscription } from 'rxjs';
import { Horario } from 'src/app/modelos/horario';
import { HorarioService } from 'src/app/services/hour/horario.service';


@Component({
  selector: 'app-desayuno',
  templateUrl: './desayuno.component.html',
  styleUrls: ['./desayuno.component.scss']
})
export class DesayunoComponent implements OnDestroy, OnInit {

  public startHour: number | null = null;
  public startMinute: number | null = null;
  public endHour: number | null = null;
  public endMinute: number | null = null;
  
  tokenSeleccionado: string = "";

  filteredDesayunoHours!: Observable<number[]>;
  filteredDesayunoMinutes!: Observable<number[]>;

  desayunoDataSubscription!: Subscription;
  desayunoData: Horario = {
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
    this.desayunoData = {
      id: null,
      startHour: null,
      startMinute: null,
      endHour: null,
      endMinute: null,
      tipoComida: null
    };
    this.initializeDesayunoTimes();
  }

  enviarDatosDesayuno(): void {
    // Aquí envías los datos del desayuno al servicio HorarioService
    if (this.startHour !== null && this.startMinute !== null && this.endHour !== null && this.endMinute !== null) {
      this.desayunoData.id = 2;
      // Asigna los valores a las propiedades de desayunoData
      this.desayunoData.startHour = this.startHour;
      this.desayunoData.startMinute = this.startMinute;
      this.desayunoData.endHour = this.endHour;
      this.desayunoData.endMinute = this.endMinute;
      this.desayunoData.tipoComida = "desayuno";
  
      // Luego, llama al método actualizarDesayuno del servicio HorarioService
      this.horarioService.actualizarComida(this.desayunoData);
      this.horarioService.enviarComida(this.desayunoData,this.desayunoData.id);
      this.cdr.detectChanges();
    } else {
      console.log("Por favor, seleccione todos los valores antes de enviar.");
    }
  }

  private initializeDesayunoTimes(): void {
    const hours = Array.from({ length: 24 }, (v, k) => k); // [0, 1, 2, ..., 23]
    const minutes = Array.from({ length: 60 }, (v, k) => k); // [0, 1, 2, ..., 59]
    this.filteredDesayunoHours = of(hours);
    this.filteredDesayunoMinutes = of(minutes);
  }

  ngOnDestroy() {
    if (this.desayunoDataSubscription) {
      this.desayunoDataSubscription.unsubscribe();
    }
  }
}
