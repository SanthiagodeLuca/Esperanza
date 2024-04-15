package colegio.comedor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import colegio.comedor.interfaceService.IPeticionesService;

@Service
public class PeticionesService implements IPeticionesService{

	
	@Autowired
	private RestTemplate restTemplate ;
	
	@Override
	public String obtenerInformacion() {
		restTemplate = new RestTemplate();
		// TODO Auto-generated method stub
		 String url = "http://localhost:8085/listar"; // Cambia la URL según tu configuración

	        // Realizar la solicitud GET y obtener la respuesta como un objeto Datos
	      
		 return restTemplate.getForObject(url, String.class);
	       	

	}

	
	
	
} 
