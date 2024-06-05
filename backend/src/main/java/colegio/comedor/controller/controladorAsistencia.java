package colegio.comedor.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.MiClase;
import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.interfaceService.IHorarioService;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Asistencia;
import colegio.comedor.service.AsistenciaService;
import colegio.comedor.service.ModificacionHorarioService;
import colegio.comerdor.filter.modelo.AsistenciaFilter;
@RestController
@RequestMapping("/api/asistencias") 
//@CrossOrigin(origins= {"http://localhost:4200"})
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
		
		  
		//  System.out.println("Received startDate: " + data.getStartDate());
		//  System.out.println("Received endDate: " + data.getEndDate());

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
	
	@GetMapping("/cambiarAsistencia/{id}")
	public String editar(@PathVariable int id,Model modelo) {
		Optional<Asistencia>almuerzo=serviceAsistencia.listarId(id);
		modelo.addAttribute("almuerzo", almuerzo);
		return "formularioAsistencia";
	}

	@GetMapping("/eliminarAsistencia/{id}")
	public String delete(@PathVariable int id) {
		serviceAsistencia.delete(id);
		return "redirect:/asistencias";
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

	
	
	
	
	

/*	@GetMapping("/asistencias")
	public String listar(Model modelo) {
		
		modelo.addAttribute("mensaje",globalVariable.getProvicionalVariable());
		   
		
	       if(inicio !=0) {
	    
	    	   globalVariable.setProvicionalVariable(globalVariable.getMientrasVariable());
	       System.out.println("actualiza variable "+globalVariable.getProvicionalVariable());
	       
	       }
	       inicio=1;
	   
	      
		  List<Asistencia> asistencias = serviceAsistencia.listar();// Asegúrate de tener este método en tu servicio
	      modelo.addAttribute("asistencias", asistencias);
	      modelo.addAttribute("miTablaAsistencias","tablaAsistencias");
		
	     // throw new ApiRequestException("se intengo Ingresar al mismo estudiante");
	      return "asistencias";
	}*/

}
