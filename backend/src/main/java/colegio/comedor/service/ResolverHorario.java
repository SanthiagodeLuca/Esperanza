package colegio.comedor.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaceService.IAsistenciaService;
import colegio.comedor.interfaceService.IHorarioService;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Asistencia;
import colegio.comedor.modelo.Horario;


@Service
public class ResolverHorario {
	
	@Autowired
	private IAsistenciaService dataAsistencia;
	
	@Autowired
	private IAlmuerzoService dataAlmuerzo;
	
	@Autowired
	private IHorarioService dataHorario;
	
	 public Almuerzo determinarHorario(Asistencia asistencia) {
		
		LocalDateTime fecha=dateToLocalDateTime(asistencia.getFecha());
		
		  
		List<Horario> horarios = dataHorario.listar();
		    for (Horario horario: horarios) {
		        int startHour = horario.getStartHour();
		        int startMinute = horario.getStartMinute();
		        int endHour = horario.getEndHour();
		        int endMinute = horario.getEndMinute();
		        
		        LocalDateTime startTime = fecha.withHour(startHour).withMinute(startMinute);
		        LocalDateTime endTime = fecha.withHour(endHour).withMinute(endMinute);
		        
		        // Verificar si la fecha proporcionada cae dentro del rango de tiempo del tipo de comida
		        if (!fecha.isBefore(startTime) && !fecha.isAfter(endTime)) {
		        	
		            return dataAlmuerzo.listarId(horario.getId()).orElse(null);
		        }
		    }
		
		
		return null;
	}
	 
	 @Autowired
	    private ApplicationContext applicationContext;

	    public void checkServiceBean() {
	        // Verificar si el bean está presente en el contexto de la aplicación
	        boolean isServiceBeanPresent = applicationContext.containsBean("ResolverHorario"); // Reemplaza "nombreDelBean" por el nombre del bean o su clase
	        System.out.println("¿El bean está presente? " + isServiceBeanPresent);
	    }
	
	
    public static LocalDateTime dateToLocalDateTime(Date date) {
        // Obtener un Instante desde el objeto Date
        Instant instant = date.toInstant();

        // Convertir el Instante a LocalDateTime utilizando la zona horaria por defecto
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        return localDateTime;
    }
}
