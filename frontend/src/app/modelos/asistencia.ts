import { Estudiante } from "./estudiante";

export interface Asistencia {
    id:number;
    estudiante:number;//clave foranea
    almuerzo:number;//clave forane
    fecha:Date;

}
