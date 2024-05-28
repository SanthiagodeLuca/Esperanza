import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AgregarCabeceraService {

  constructor() { }
  /*
  this.http.get(API_URL_IMAGEN + estudiante.id + '.png', { headers:headers, responseType: 'arraybuffer' }).subscribe(
    (imagenData: ArrayBuffer) => {
      // Convertir los bytes en una URL de objeto
      const imagenBlob = new Blob([imagenData]);
      this.imagenUrl = URL.createObjectURL(imagenBlob);
    },
    error => {
      console.error('Error al cargar la imagen:', error);
    }
  );*/
}
