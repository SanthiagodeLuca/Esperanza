package colegio.comedor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

	@RestController()
	public class controladorImagenes {
		 @GetMapping("api/images/{id}")
		    public ResponseEntity<InputStreamResource> obtenerImagen(@PathVariable String id) throws IOException {
		

			 
		        String rutaImagen = "src/main/resources/static/images/"+id+".png";
		        
		        
		        // Lee el archivo de la ruta proporcionada
		        File archivo = new File(rutaImagen);
		        FileInputStream inputStream = new FileInputStream(archivo);
	
		        // Devuelve la imagen como una respuesta con el tipo de contenido adecuado
		        return ResponseEntity.ok()
		                .contentType(org.springframework.http.MediaType.IMAGE_PNG)
		                .body(new InputStreamResource(inputStream));
		    }
	}
