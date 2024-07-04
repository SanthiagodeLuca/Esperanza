package colegio.comedor.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import colegio.comedor.interfaceService.IUserService;
import colegio.comedor.modelo.User;

@RestController
@RequestMapping("/api/user") 
//@CrossOrigin(origins= {"http://localhost:4200"})
public class controladorUser {
	@Autowired
	private IUserService datosUsuarios;
	
	private static final Logger logger = LoggerFactory.getLogger(controladorUser.class);

	
	@GetMapping("/{userId}") // Elimina "/api/user" de la ruta
    public ResponseEntity devolverUsuario(@PathVariable Optional<Double> userId) {
        if (userId.isPresent()) {
            double id = userId.get(); // Obtiene el valor del Optional<Double>
            Optional<User> user = datosUsuarios.buscarUsuario(id); // Llama al método con el valor primitivo
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("El parámetro userId es obligatorio");
        }
    }
	
	
	@GetMapping
    public ResponseEntity<List<User>> getEstudiantes() {
        List<User> usuarios = datosUsuarios.listar();
        return ResponseEntity.ok(usuarios);
    }
	
	
	@PutMapping("/cambiarUser/{id}")
	public ResponseEntity editarUser(@PathVariable String id,@RequestBody User user) {
		Optional<User>userActualizar=datosUsuarios.listarId(id);
		  if (userActualizar.isPresent()) {
	        //    estudianteActualizar.setId(estudiante.getId());
	            userActualizar.get().setFirstname(user.getFirstname());
	            userActualizar.get().setCountry(user.getCountry());
	            userActualizar.get().setRole(user.getRole());
	            userActualizar.get().setUsername(user.getUsername());
	            
	            
	            // Guardar la instancia actualizada
	            datosUsuarios.save(userActualizar.get());
	            logger.info("editando asistencia: " + userActualizar);

	            return ResponseEntity.ok(userActualizar);
	        } else {
	            // Manejar el caso en que la asistencia no fue encontrada
	            return ResponseEntity.notFound().build();
	        
	        }
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)

	@DeleteMapping("/eliminarUsuario/{id}")
	public ResponseEntity eliminarUsuario(@PathVariable String id) {
		
		datosUsuarios.delete(id);
		
		
		
		return ResponseEntity.ok("usuario eliminado ");
	}
	
	
	
	
}
