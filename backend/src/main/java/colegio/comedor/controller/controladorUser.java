package colegio.comedor.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import colegio.comedor.interfaceService.IUserService;
import colegio.comedor.modelo.User;

@RestController
@RequestMapping("/api/user") 
//@CrossOrigin(origins= {"http://localhost:4200"})
public class controladorUser {
	@Autowired
	private IUserService datosUsuarios;
	
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
	

}
