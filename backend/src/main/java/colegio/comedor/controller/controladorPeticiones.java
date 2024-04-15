package colegio.comedor.controller;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import colegio.comedor.MiClase;
import colegio.comedor.horario;
import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaceService.IAsistenciaService;
import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.modelo.JsonRequest;
import colegio.comedor.service.BaseDatosService;

@Controller
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class controladorPeticiones {	
	
	@Autowired
	private MiClase variablesGlobales;
	
	@Autowired
	private IAsistenciaService serviceAsistencia;

	
	@Autowired
	private IEstudianteService serviceEstudiante;
	
	
	@Autowired
	private IAlmuerzoService serviceAlmuerzo;
	
	@Autowired
	private BaseDatosService serviceBaseDatos;
	
	
	
	  private static final Logger logger = LoggerFactory.getLogger(controladorPeticiones.class);
	  public void tuMetodo() {
	        logger.debug("Este es un mensaje de debug");
	        logger.info("Este es un mensaje de información");
	        logger.warn("Este es un mensaje de advertencia");
	        logger.error("Este es un mensaje de error");
	    }
	   
	  @Autowired
	  private RestTemplate restTemplate;

	   
	  @GetMapping("/calendario")
	  public String mostrarHorarios() {


		  
		  return "horarios";
	  }
	  
	  @PostMapping("/horario")
	  public void calendario(@RequestBody horario horario) {
		  
		  String horaInicioDesayuno = horario.getHoraInicialDesayuno();
		    String minInicioDesayuno = horario.getMinutoInicialDesayuno();
		    String horaFinDesayuno = horario.getHoraFinalDesayuno();
		    String minFinDesayuno = horario.getMinutoFinalDesayuno();

		    // Imprimir los datos en la consola
		    System.out.println("Hora Inicio Desayuno: " + horaInicioDesayuno);
		    System.out.println("Minuto Inicio Desayuno: " + minInicioDesayuno);
		    System.out.println("Hora Final Desayuno: " + horaFinDesayuno);
		    System.out.println("Minuto Final Desayuno: " + minFinDesayuno);

	  }
	  
	  
	    @PostMapping("/prueba")
	    public String procesar(@RequestBody JsonRequest request) {
	    	System.out.println("Datos recibidos: " + request.getData());
	    	return "datos recibidos";
	    }
	    
	
	 
	    @PostMapping("/generarAsistenciaQR")
	    public ModelAndView procesarQR(@RequestBody Map<String,String> contenidoQR, Model model) throws RestClientException {
	    	
	    	
	    	    
	    	    
	    	  logger.info("Se ha iniciado el procesamiento del QR.");
	    	  logger.error("aparecio");
	    	    System.out.println("entro");
	    
	    	    int valor=serviceAsistencia.guardarAsistenciaQR(contenidoQR);
	    	    
	    	   
	    	    logger.info("aparecio",valor);
	    	    if ( valor == 1) {
	    	     
	    	    	logger.info("aparecio");
	    	     
	    	     ModelAndView mav = new ModelAndView();	
	    	     
	    	     variablesGlobales.setProvicionalVariable(variablesGlobales.getErrorVariable());
	    	    
	    	     mav.addObject("errorAlert", "No se puede insertar una fila con la misma categoría");
	    	   
	    	     mav.setViewName("error"); 
	    	     
	    	     return mav; 
	    	     
	    	    } else if(valor==0){
	    	    	
	    	    	 ModelAndView mav = new ModelAndView();	
	    	    	 logger.info("guardo correctamente.");
		    	     mav.addObject("entro", "se adiciono correctamente");
		    	     variablesGlobales.setProvicionalVariable(variablesGlobales.getMiVariable());
		    	     
		    	   //  variablesGlobales.setActualizar("No");
		 	        /*
		    	     mav.setViewName("error");
		    	     return mav;*/
		    	     String script = "<script>recargarPagina();</script>";
		    	     return new ModelAndView("asistencias").addObject("reloadScript", script);

	    	    	
	    	    }else {
	    	    //	modelo.addAttribute("miTexto", "ejecutar");
	    	    	variablesGlobales.setActualizar("No");
	    	        logger.error("Error al procesar el QR.");
	    	        return new ModelAndView("Bienvenidos"); // Reemplaza "error" con el nombre de tu vista de error
	    	    }
	    
	        
	    	
	    	
	    }
	    
	    /*
	    @ExceptionHandler(value = {org.springframework.orm.jpa.JpaSystemException.class})
	    public ModelAndView handleJpaSystemException(org.springframework.orm.jpa.JpaSystemException ex, Model model) {
	        ModelAndView mav = new ModelAndView();
	       logger.info("se crea le mav");
	        mav.addObject("errorAlert", "No se puede insertar una fila con la misma categoría");
	        mav.setViewName("asistencia"); // Nombre de la vista de error
	        return mav;
	    }*/
	    
	    
	 
}



