package colegio.comedor.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import colegio.comedor.modelo.Asistencia;
import colegio.comedor.service.AsistenciaService;
import colegio.comedor.service.ExcelService;
@Component
public class RealizarInforme {

	
	@Autowired
    private ExcelService excelService; // Asegúrate de tener un servicio para el Excel
	
	@Autowired
	private AsistenciaService asistenciaService;
	
	private List<List<String>> obtenerDatos() {
	    List<List<String>> datos = new ArrayList<>();
	    List<Asistencia> asistencias = asistenciaService.listar(); // Supongamos que operacionAsistencia tiene un método listar() que obtiene las asistencias
	    List<String> filaDatos = new ArrayList<>();
	    for (Asistencia asistencia : asistencias) {
	      
	    
	        filaDatos.add(String.valueOf(asistencia.getId()));
	        filaDatos.add(String.valueOf(asistencia.getEstudiante().getId()));
	        filaDatos.add(String.valueOf(asistencia.getFecha()));
	        filaDatos.add(String.valueOf(asistencia.getAlmuerzo().getNombre()));
	   

	        datos.add(filaDatos);    
	        
	   }
	    System.out.println("datos"+datos);

	    return datos;
	}
    @Scheduled(cron = "00 52 18 * * ?") // Ejecutar todos los días a las 14:00
    public void ejecutarTarea() {
        try {
        	
        	excelService.actualizarExcel(obtenerDatos());
            System.out.println("Datos actualizados correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al actualizar los datos.");
        }
    }
}
