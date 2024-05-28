package colegio.comedor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import colegio.comedor.GeneradorQR;
import colegio.comedor.MiClase;
import colegio.comedor.interfaceService.IAsistenciaService;
import colegio.comedor.interfaces.InterfazAsistencia;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Asistencia;
import colegio.comedor.modelo.Estudiante;

@Service
public class AsistenciaService  implements IAsistenciaService{

	@Autowired
	private HorarioService horarioService;
	
	@Autowired
	private MiClase variablesGlobales;
	@Autowired
	private EstudianteService interfazEstudiante;
	
	@Autowired
	private AlmuerzoService interfazAlmuerzo;
	
	
	@Autowired
	private InterfazAsistencia dataAsistencia;
	
	@Override
	public List<Asistencia> listar() {
		// TODO Auto-generated method stub
		return (List<Asistencia>)dataAsistencia.findAll();
	}

	@Override
	public Optional<Asistencia> listarId(int id) {
		// TODO Auto-generated method stub
		return dataAsistencia.findById(id);
	}

	@Override
	public int save(Asistencia e) {
		// TODO Auto-generated method stub
		int res=0;
		
		Asistencia estudiante=dataAsistencia.save(e);;
		if(!estudiante.equals(null)) {
			res=1;
			
		}
		return res;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		System.out.println("que paso");
		dataAsistencia.deleteById(id);
	}

	@Override
	public Date guardarFecha(int hora,int minuto) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hora);
        calendar.set(java.util.Calendar.MINUTE, minuto);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTime();
		
	}

	@Override
	public int guardarAsistenciaQR(Map<String, String> contenidoQR) {
	    
		int horaInicialDesayuno=variablesGlobales.getHoraInicialDesayunoVariable(); 
			int horaFinalDesayuno=variablesGlobales.getHoraFinalDesayunoVariable(); 
        int minutoInicialDesayuno=variablesGlobales.getMinutoInicialDesayunoVariable(); 
         int minutoFinalDesayuno=variablesGlobales.getMinutoFinalDesayunoVariable();
         
         
        int horaInicialAlmuerzo=variablesGlobales.getHoraAlmueroVariable(); 
        int horaFinalAlmuerzo=variablesGlobales.getHoraFinalAlmuerzoVariable(); 
        int minutoInicialAlmuerzo=variablesGlobales.getMinutoInicialAlmuerzoVariable(); 
        int minutoFinalAlmuerzo=variablesGlobales.getMinutoFinalAlmuerzoVariable(); 
		
		int valorEsperado = 0;
	   
	    Asistencia asistencia = new Asistencia();
	    
	    
	    Optional<Estudiante> estudiante = interfazEstudiante.listarId(GeneradorQR.desencriptarAES(contenidoQR.get("estudiante")));

	    asistencia.setFecha(new Date());

	    Optional<Almuerzo> almuerzo = interfazAlmuerzo.listarId(3);

	    if (asistencia.getFecha().after(guardarFecha(horaInicialDesayuno, minutoInicialDesayuno)) && asistencia.getFecha().before(guardarFecha(horaFinalDesayuno, minutoFinalDesayuno))) {
	        almuerzo = interfazAlmuerzo.listarId(1);
	    } else if (asistencia.getFecha().after(guardarFecha(horaInicialAlmuerzo, minutoInicialAlmuerzo)) && asistencia.getFecha().before(guardarFecha(horaFinalAlmuerzo, minutoFinalAlmuerzo))) {
	        almuerzo = interfazAlmuerzo.listarId(2);
	    }
	    asistencia.setEstudiante(estudiante.get());
	    asistencia.setAlmuerzo(almuerzo.get());

	    try {
	        System.out.println("se intento");
	        save(asistencia); // Llama al método save de tu servicio
	    } catch (org.springframework.orm.jpa.JpaSystemException ex) {
	        valorEsperado = 1;
	        
	    }
	   
	    return valorEsperado;
	}
	
	@Override
	public void limpiar() {
		// TODO Auto-generated method stub
		dataAsistencia.deleteAll();
		}

}
