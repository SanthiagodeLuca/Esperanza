package colegio.comedor.controller;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.MiClase;
import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaceService.IAsistenciaService;
import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.modelo.Asistencia;


import reactor.core.publisher.Flux;
@RestController
@RequestMapping("/api/asistencias") 
public class controladorAsistencia{
	private static final Logger logger = LoggerFactory.getLogger(controladorAsistencia.class);

	@Autowired
	private IAsistenciaService serviceAsistencia;

	
	@Autowired
	private IEstudianteService serviceEstudiante;
	
	
	@Autowired
	private IAlmuerzoService serviceAlmuerzo;
	
	
	@Autowired
	private MiClase globalVariable;
	
	private int inicio=0;
	
	
	
	
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

	@GetMapping("/asistencias")
	public String listar(Model modelo) {
		
		modelo.addAttribute("mensaje",globalVariable.getProvicionalVariable());
		   
		
	       if(inicio !=0) {
	    
	    	   globalVariable.setProvicionalVariable(globalVariable.getMientrasVariable());
	       System.out.println("actualiza variable "+globalVariable.getProvicionalVariable());
	       
	       }
	       inicio=1;
	    /*   if(globalVariable.getActualizar()=="") {
	       modelo.addAttribute("reloadScript",globalVariable.getActualizar());
	       globalVariable.setActualizar(true);
	       modelo.addAttribute("reloadScript",globalVariable.getActualizar());
	       }*/
	       
	       // TODO Auto-generated method stub
		  List<Asistencia> asistencias = serviceAsistencia.listar();// Asegúrate de tener este método en tu servicio
	      modelo.addAttribute("asistencias", asistencias);
	      modelo.addAttribute("miTablaAsistencias","tablaAsistencias");
		
	     // throw new ApiRequestException("se intengo Ingresar al mismo estudiante");
	      return "asistencias";
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
		return ResponseEntity.ok(asistencias);
		
		
	}
}
