import { Component } from '@angular/core';
import { ExcelServiceService } from '../services/excel-service.service';

@Component({
  selector: 'app-excel',
  templateUrl: './excel.component.html',
  styleUrls: ['./excel.component.scss']
})
export class ExcelComponent {


  constructor(private excel:ExcelServiceService){}
  mensaje: string | undefined;

    cargaArchivo(event :any){



      const archivo=event.target.files[0];//este es el archivo que el usuario cogio

      this.excel.enviarArchivoCSV(archivo).subscribe(

        response=>{
          console.log('archivo enivado correctamente al backend',response);
          this.mensaje=response.message;
        },
        error=>{

          console.log('Error al enviar el archivo CSV al backend',error);
          this.mensaje = error.message;
        }
      )



    }

  }

