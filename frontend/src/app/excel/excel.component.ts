import { Component } from '@angular/core';
import { ExcelServiceService } from '../services/excel-service.service';
import * as XLSX from 'xlsx';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { LoginService } from '../services/auth/login.service';

@Component({
  selector: 'app-excel',
  templateUrl: './excel.component.html',
  styleUrls: ['./excel.component.scss']
})
export class ExcelComponent {
  mensaje: string | undefined;
  mensajeTipo: 'success' | 'error' | undefined; // To hold the type of message
  archivo: File | undefined;

  startDate: Date | null = null;
  endDate: Date | null = null;

  tokenSeleccionado: string = '';

  constructor(private excel: ExcelServiceService, private http: HttpClient, private loginService: LoginService) {
    this.loginService.userToken.subscribe(
      (token: String) => {
        this.tokenSeleccionado = token.toString();
      },
      (error: any) => {
        console.error('Error al obtener el token de usuario:', error);
      }
    );
  }

  cargaArchivo(event: any) {
    this.archivo = event.target.files[0];
  }

  validarArchivo() {
    const requiredHeaders = [
      'NRO_DOCUMENTO', 'APELLIDO1', 'APELLIDO2', 'NOMBRE1', 'NOMBRE2',
      'DESCRIP_JORNADA', 'GRUPO', 'ESPECIAL'
    ];

    if (this.archivo) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });
        const worksheet = workbook.Sheets[workbook.SheetNames[0]];
        const headers = XLSX.utils.sheet_to_json(worksheet, { header: 1 })[0] as string[];

        if (headers.length === requiredHeaders.length &&
          requiredHeaders.every((header, index) => headers[index] === header)) {
          this.enviarArchivo();
        } else {
          this.mensaje = 'El archivo no tiene el formato correcto.';
          this.mensajeTipo = 'error';
        }
      };
      reader.readAsArrayBuffer(this.archivo);
    } else {
      this.mensaje = 'No se ha seleccionado ningún archivo.';
      this.mensajeTipo = 'error';
    }
  }

  enviarArchivo() {
    if (this.archivo) {
      this.excel.enviarArchivoCSV(this.archivo).subscribe(
        response => {
          console.log('Archivo enviado correctamente al backend', response);
          this.mensaje = response.message;
          this.mensajeTipo = 'success';
        },
        error => {
          console.log('Error al enviar el archivo CSV al backend', error);
          this.mensaje = error.message;
          this.mensajeTipo = 'error';
        }
      );
    }
  }

  exportExcel() {
    if (this.startDate && this.endDate) {
      const params = new HttpParams()
        .set('startDate', this.startDate.toISOString().split('T')[0])
        .set('endDate', this.endDate.toISOString().split('T')[0]);

      const headers = new HttpHeaders({
        'Authorization': `Bearer ${this.tokenSeleccionado}`
      });

      this.http.get('http://localhost:8085/api/excel/export', { params, headers, responseType: 'blob' }).subscribe({
        next: (response: Blob) => {
          saveAs(response, 'asistencias.xlsx');
          this.mensaje = 'Archivo exportado correctamente.';
          this.mensajeTipo = 'success';
        },
        error: (error) => {
          console.error('Error exporting file:', error);
          this.mensaje = 'Error al exportar el archivo.';
          this.mensajeTipo = 'error';
        }
      });
    }
  }
}
