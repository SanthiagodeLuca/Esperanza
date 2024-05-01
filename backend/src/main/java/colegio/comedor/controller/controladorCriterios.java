package colegio.comedor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.modelo.Criterio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController

@RequestMapping("/api/criterios") 	
public class controladorCriterios {

	
	@Autowired
	private colegio.comedor.service.criterioService criterioService; 

	
	
	@GetMapping
	public List<Criterio> darCriterios() {
		
		
		return criterioService.listarCriterio();
	}
	
	@PostMapping
	public ResponseEntity<String> agregarDatosCriterio(@RequestBody Criterio criterio) {
		
		try {
		criterioService.save(criterio);
		 return ResponseEntity.ok().body("{\"message\": \"criterio se agrego \"}");
		}catch(Exception e) {
			
			 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("criterio no subido: " + e.getMessage());
			
		}
		
		
	}
	
	
	
}
