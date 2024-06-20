export interface AsistenciaNueva {
    id: number;
    estudiante: {
      id: string;
      nombre: string;
      jornada: string;
      curso: string;
      imagen: string | null;
      especial: boolean;
    };
    almuerzo: {
      id: number;
      nombre: string;
    };
    fecha: string;
  }
  