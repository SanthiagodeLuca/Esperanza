import { ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subscription, of } from 'rxjs';
import { Horario } from 'src/app/modelos/horario';
import { LoginService } from 'src/app/services/auth/login.service';
import { HorarioService } from 'src/app/services/hour/horario.service';


@Component({
  selector: 'app-refrigerio',
  templateUrl: './refrigerio.component.html',
  styleUrl: './refrigerio.component.scss'
})
export class RefrigerioComponent implements OnDestroy,OnInit{
/*
  @Input() startHour: number | null = null;
  @Input() startMinute: number | null = null;
  @Input() endHour: number | null = null;
  @Input() endMinute: number | null = null;
  @Output() update = new EventEmitter<any>();

*/
public startHour: number | null = null;
public startMinute: number | null = null;
public endHour: number | null = null;
public endMinute: number | null = null;

tokenSeleccionado:string="";




  //seran inicializadas antes de ser usadas
  filteredRefrigerioHours!: Observable<number[]>;
  filteredRefrigerioMinutes!: Observable<number[]>;

  refrigerioDataSubscription!: Subscription;
  refrigerioData: Horario = {
    id:null,
    startHour: null,
    startMinute: null,
    endHour: null,
    endMinute: null,
    tipoComida:null
  }; // Inicializar con un objeto vacío para evitar errores

  constructor(private horarioService:HorarioService,private cdr:ChangeDetectorRef,private loginService:LoginService) {
   
  }

  onUpdate() {
    console.log("hola");
    //console.log(this.startHour,this.startMinute,this.endHour,this.endMinute)
  }


  ngOnInit(): void {
  

   // console.log("onUpdate() se ha llamado");
    this.refrigerioData = {
      id:null,
      startHour: null,
      startMinute: null,
      endHour: null,
      endMinute: null,
      tipoComida:null
    };
    this.initializeRefrigerioTimes();
    this.obtenerToken();
  }
  
  enviarDatosRefrigerio(): void {
    // Aquí envías los datos del refrigerio al servicio HorarioService
    if (this.startHour !== null && this.startMinute !== null && this.endHour !== null && this.endMinute !== null) {
      this.refrigerioData.id=1;
      // Asigna los valores a las propiedades de refrigerioData
      this.refrigerioData.startHour = this.startHour;
      this.refrigerioData.startMinute = this.startMinute;
      this.refrigerioData.endHour = this.endHour;
      this.refrigerioData.endMinute = this.endMinute;
      this.refrigerioData.tipoComida="refrigerio";
  
      // Luego, llama al método actualizarRefrigerio del servicio HorarioService
      this.horarioService.actualizarComida(this.refrigerioData);
      this.horarioService.enviarComida(this.refrigerioData,this.refrigerioData.id);
      this.cdr.detectChanges();
    } else {
      console.log("Por favor, seleccione todos los valores antes de enviar.");
    }
  }
  
  private initializeRefrigerioTimes(): void {
    const hours = Array.from({ length: 24 }, (v, k) => k); // [0, 1, 2, ..., 23]
    const minutes = Array.from({ length: 60 }, (v, k) => k); // [0, 1, 2, ..., 59]
    this.filteredRefrigerioHours = of(hours);
    this.filteredRefrigerioMinutes = of(minutes);
  }
  ngOnDestroy() {
    if (this.refrigerioDataSubscription) {
      this.refrigerioDataSubscription.unsubscribe();
    }
  }

  obtenerToken(){

    this.loginService.userToken.subscribe(

      (token: String) => {
        console.log('Token recibido:', token);
        // Asignar el token a la propiedad tokenSeleccionado
        this.tokenSeleccionado = token.toString();
        // Otro código aquí...
      },
      (error: any) => {
        // Manejar cualquier error que ocurra al obtener el token
        console.error('Error al obtener el token de usuario:', error);
      }
    );

    
  }
}
