import { Estudiante } from "./estudiante";

export interface Asistencia {
    id:number;
    estudiante:number;//clave foranea
    almerzo:number;//clave forane
    fecha:Date;

}
