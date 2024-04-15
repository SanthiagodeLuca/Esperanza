package colegio.comedor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.modelo.Almuerzo;

@RestController
@RequestMapping("/api/comidas")
public class controladorComidas {
	
	@Autowired
	private IAlmuerzoService comidasService;
	
	
	
@GetMapping
public ResponseEntity<List<Almuerzo>> getComidas(){
	
	List<Almuerzo>comidas=comidasService.listar();
	return ResponseEntity.ok(comidas);
}

	
}
