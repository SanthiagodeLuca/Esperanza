package colegio.comedor.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.GeneradorQR;
import colegio.comedor.MiClase;
import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.interfaceService.IHorarioService;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Asistencia;
import colegio.comedor.modelo.Estudiante;
import colegio.comedor.service.AsistenciaService;
import colegio.comedor.service.ModificacionHorarioService;
import colegio.comerdor.filter.modelo.AsistenciaFilter;
@RestController
@RequestMapping("/api/asistencias") 
@CrossOrigin(origins= {"http://localhost:4200"})
public class controladorAsistencia{
	private static final Logger logger = LoggerFactory.getLogger(controladorAsistencia.class);
	
	 
	 private SimpMessagingTemplate messagingTemplate;
	 
	  @Autowired
	    public controladorAsistencia(SimpMessagingTemplate messagingTemplate) {
	        this.messagingTemplate = messagingTemplate;
	    }

	@Autowired
	private AsistenciaService serviceAsistencia;

	
	@Autowired
	private IEstudianteService serviceEstudiante;
	
	
	@Autowired
	private IAlmuerzoService serviceAlmuerzo;
	
	@Autowired
	private IHorarioService serviceHorario;
	
	
	@Autowired
	private MiClase globalVariable;
	
	private int inicio=0;
	
	
	  @PostMapping("/fecha")
	  // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	  //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	    public List<Asistencia> obtenerAsistencias(@RequestBody AsistenciaFilter data) {
		 // Map<String, Date> filterParams = AsistenciaFilter.parseJsonToMap(data);
		
		  logger.info("Received startDate: " + data.getStartDate());
	        logger.info("Received endDate: " + data.getEndDate());
		  System.out.println("Received startDate: " + data.getStartDate());
		  System.out.println("Received endDate: " + data.getEndDate());

	        return serviceAsistencia.obtenerFechasAsistencia(data.getStartDate(), data.getEndDate());
	     //   return null;
	    }
	
	@GetMapping("/queso")
	    public String mostrarError(Model modelo) {
	      
			modelo.addAttribute("mensaje",globalVariable.getProvicionalVariable());
	   
			
	       if(inicio !=0) {
	    
	    	   globalVariable.setProvicionalVariable(globalVariable.getMientrasVariable());
	       System.out.println("actualiza variable "+globalVariable.getProvicionalVariable());
	       
	       }
	       inicio=1;
	       return "error";
	    }

	@GetMapping("/newAsistencia")
	public String newAsistencia(Model modelo) {
		// TODO Auto-generated method stub
		modelo.addAttribute("asistencia", new Asistencia());
				
		return "formularioAsistencia";
	}

	
	@PostMapping("/guardarAsistencia")
	public String guardarAsistencia(@Validated Asistencia e) {
		 serviceAsistencia.save(e);
		return "redirect:/asistencias";
	}
	
	@PutMapping("/cambiarAsistencia/{id}")
    public ResponseEntity<?> editar(@PathVariable int id, @RequestBody Asistencia asistencia) {
        Optional<Asistencia> asistenciaActualizarOpt = serviceAsistencia.listarId(id);
        if (asistenciaActualizarOpt.isPresent()) {
            Asistencia asistenciaActualizar = asistenciaActualizarOpt.get();
            asistenciaActualizar.setEstudiante(asistencia.getEstudiante());
            asistenciaActualizar.setAlmuerzo(asistencia.getAlmuerzo());
            asistenciaActualizar.setFecha(asistencia.getFecha());
            
            // Guardar la instancia actualizada
            serviceAsistencia.edit(asistenciaActualizar);
            logger.info("editando asistencia: " + asistencia);

            return ResponseEntity.ok(asistenciaActualizar);
        } else {
            // Manejar el caso en que la asistencia no fue encontrada
            return ResponseEntity.notFound().build();
        }
    }
	
	@DeleteMapping("/eliminarAsistencia/{id}")
	public ResponseEntity<?>  delete(@PathVariable int id) {
		Optional<Asistencia> asistencia=serviceAsistencia.listarId(id);
		if(asistencia.isPresent()) {
			serviceAsistencia.delete(id);
			 return ResponseEntity.noContent().build(); // 204 No Content

			
		}else {
			 return ResponseEntity.notFound().build(); // 404 Not Found
			}
		}
	
	
	@GetMapping("/limpiarAsistencia")
	public String limpiarEstudiantes(Model model) {
		serviceAsistencia.limpiar();
		
		
		return "redirect:/asistencias";
	}
	@GetMapping("/sse")
	public String streamEvents() {
	    return "redirect:/asistencias"; // Redireccionar a la página de asistencias
	}
	
	
	@GetMapping
	public ResponseEntity <List<Asistencia>>getAsistencias() {
		
		List asistencias=serviceAsistencia.listar();
		//System.out.println();
		return ResponseEntity.ok(asistencias);
		
		
	}
	@PostMapping("/nuevaAsistencia")
	public ResponseEntity<String> nuevaAsistencia(@RequestBody Asistencia nuevaAsistencia) {
		
		String valor=nuevaAsistencia.getEstudiante().getId()+"";
		
	//	Optional<Estudiante> estudiante=serviceEstudiante.listarId(GeneradorQR.desencriptarAES(valor));
	
	/*	if(estudiante!=null) {
			nuevaAsistencia.setEstudiante(estudiante);
			
		}*/
		
	    // Aquí puedes agregar la lógica para guardar la nueva asistencia en tu base de datos
	    // Por ejemplo:
	 
		ModificacionHorarioService resolverHorario = new ModificacionHorarioService(serviceAlmuerzo,serviceHorario);
		Almuerzo almuerzo = resolverHorario.determinarHorario(nuevaAsistencia);
		nuevaAsistencia.setAlmuerzo(almuerzo);
		
		
		
	    serviceAsistencia.save(nuevaAsistencia);
	    
	    logger.info("Enviando nueva asistencia: " + nuevaAsistencia);

	    //solicitud TCP 
	 // Enviar la asistencia actualizada a través de WebSocket
        messagingTemplate.convertAndSend("/topic/asistencias", nuevaAsistencia);
	    
	    // Puedes enviar una respuesta con un mensaje indicando que la asistencia se ha guardado correctamente
	    return ResponseEntity.ok("Asistencia guardada correctamente");
	}

	
	
	
	
	



}
