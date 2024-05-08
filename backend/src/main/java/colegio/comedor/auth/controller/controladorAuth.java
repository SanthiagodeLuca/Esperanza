	package colegio.comedor.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.service.AuthService;
import lombok.RequiredArgsConstructor;


/*
 * import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MiClase {
    private final String campo1;
    private final int campo2;
    private final boolean campo3;
    private final OtroObjeto campo4;
}
mel crea este constructor automaticamente
 * public MiClase(String campo1, int campo2, boolean campo3, OtroObjeto campo4) {
    this.campo1 = campo1;
    this.campo2 = campo2;
    this.campo3 = campo3;
    this.campo4 = campo4;
}

 * */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class controladorAuth {

	@Autowired
	private final AuthService authService;
	
	 private final PasswordEncoder passwordEncoder;
	@PostMapping(value="login")
	public ResponseEntity<authResponse> login(@RequestBody loginResquest request) {
		return ResponseEntity.ok(authService.login(request));
	}
	//especifica la url que se asociara con el metodo 
	@PostMapping(value="register")
	public ResponseEntity<authResponse> register(@RequestBody RegisterRequest request){
		if (passwordEncoder == null) {
	        throw new IllegalStateException("PasswordEncoder no ha sido inicializado.");
	    }
		return ResponseEntity.ok(authService.register(request));
	}
	
	
}
