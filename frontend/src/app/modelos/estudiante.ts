export interface Estudiante {

    id: string;
  nombre: string;
  jornada: string;
  curso: string; // Agregar propiedad curso si no está presente
  imagen: string | null;
  especial: boolean;
  asistencias: any[]; 
  imagenVisible:boolean;
  imagenUrl:string;
  [key: string]: any; // Firma de índice para aceptar cualquier propiedad adicional
}
