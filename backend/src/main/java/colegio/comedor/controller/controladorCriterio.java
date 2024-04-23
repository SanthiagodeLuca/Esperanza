package colegio.comedor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.interfaceService.ICriterioService;

@RestController
@RequestMapping("/api/criterio")
public class controladorCriterio {

	@Autowired
	private ICriterioService criterio;
	
	
	
	
	
}
