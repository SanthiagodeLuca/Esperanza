package colegio.comedor.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/imagen")
public class controladorImagen {

	//pone el valor de apllication properties en rutaImagenes
	@Value("${ruta.imagenes}")
	private String rutaImagenes;
	
	@GetMapping("/{nombreImagen}")
	public ResponseEntity<String> obtenerUrlImagen(@PathVariable String nombreImagen){
		
		String urlImagen=rutaImagenes+"/"+nombreImagen;
		return ResponseEntity.ok(urlImagen);
		
	}	
	
	//falta explicacion aqui
	 @GetMapping("/image/{filename:.+}")
	    public ResponseEntity<byte[]> getImage(@PathVariable String filename) throws IOException {
	        // Ruta del directorio donde se almacenan las imágenes
	        String directory = "src/main/resources/static/images/";
	        
	        // Obtiene la ruta completa de la imagen
	        Path imagePath = Paths.get(directory + filename);
	        
	        // Verifica si la imagen existe
	        if (!Files.exists(imagePath)) {
	            return ResponseEntity.notFound().build();
	        }
	        
	        // Lee la imagen y lo guarda en byte
	        byte[] imageBytes = Files.readAllBytes(imagePath);
	        
	        // Configura la respuesta con el contenido de la imagen y el tipo de contenido
	        // le dice co mediaType que el contenido es una imagen y que lo que se envia son los bytes
	        return ResponseEntity.ok()
	                .contentType(MediaType.IMAGE_PNG) // Ajusta el tipo de contenido según el tipo de imagen
	                .body(imageBytes);
	    }
}
