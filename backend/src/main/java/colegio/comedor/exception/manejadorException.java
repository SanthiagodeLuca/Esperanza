package colegio.comedor.exception;

import java.util.NoSuchElementException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//@ControllerAdvice
public class manejadorException {
	
	/*
	@ExceptionHandler(value = {ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
		
		
		//create payload containing 
		HttpStatus badRequest=HttpStatus.BAD_REQUEST;
		
		ApiException apiException=new ApiException(e.getMessage(),
				e,
				HttpStatus.BAD_REQUEST,
				ZonedDateTime.now(ZoneId.of("z"))
				);
		//return response entity
		
		return new ResponseEntity<>(apiException,badRequest);
	}*/
	
    //@ExceptionHandler(value = {NoSuchElementException.class})
    public String handleNoSuchElementException(NoSuchElementException ex,Model model) {
       
    	System.out.println("esta acu ya");
        model.addAttribute("errorAlert", ex.getMessage());
        System.out.println("Modelo: " + model);

        return "asistencias";
    	// Map<String, String> responseBody = new HashMap<>();
        //responseBody.put("errorAlert", ex.getMessage());
        //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
    
  /* @ExceptionHandler(value = {org.springframework.orm.jpa.JpaSystemException.class})
    public String databaseError(org.springframework.orm.jpa.JpaSystemException ex,Model model) {
    	
    	model.addAttribute("errorAlert",ex.getMessage());
    	
    	return "asistencias";
    

}
   /* public ModelAndView handleJpaSystemException(org.springframework.orm.jpa.JpaSystemException ex,Model model) {
      System.out.println("esta aqui "+ex.getMessage());
      
      ModelAndView mav = new ModelAndView();
     mav.addObject("errorAlert", ex.getMessage());
     mav.setViewName("asistencias");



      return mav; // Nombre de la vista de error
     // System.out.println("responseBodu"+reponseBody);
    /*	Map<String, String> responseBody = new HashMap<>();
        responseBody.put("errorAlert", "No se puede insertar una fila con la misma categoría");
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);*/
        
    /*}
    /*
    @ExceptionHandler(value = {org.springframework.orm.jpa.JpaSystemException.class})
    public ResponseEntity<Object> ExcepcioninsercionDatosRepetidos(org.springframework.orm.jpa.JpaSystemException ex,Model model) {
      System.out.println("esta aqui "+ex.getMessage());
      
  		HttpStatus badRequest=HttpStatus.BAD_REQUEST;
  		ApiException apiException=new ApiException(ex.getMessage(),
				ex,
				HttpStatus.BAD_REQUEST,
				ZonedDateTime.now(ZoneId.of("z"))
				);
      return new ResponseEntity<>(apiException,badRequest); // Nombre de la vista de error
    
        
    }*/
}
