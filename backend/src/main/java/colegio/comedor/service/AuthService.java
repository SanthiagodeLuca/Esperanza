package colegio.comedor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import colegio.comedor.Role;
import colegio.comedor.auth.controller.RegisterRequest;
import colegio.comedor.auth.controller.authResponse;
import colegio.comedor.auth.controller.loginResquest;

import colegio.comedor.interfaces.InterfazUser;
import colegio.comedor.modelo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private userService userRepository;

	private final JwtService jwtService;
	
	@Autowired
	private final AuthenticationManager authenticationManager;
	//private final PasswordEncoder passwordEncoder;
	
	//lo que hace en coger la clase login request y obtener de ella 
	//el nombre y contraseña del usuario que ingreso
	public authResponse login(loginResquest request) {
	    try {
	        List<User> users = userRepository.listar(); // Obtener la lista de todos los usuarios
	        System.out.println("Lista de usuarios:");
	        for (User user : users) {
	            System.out.println(user);
	        }
	        
	        // UsernamePasswordAuthenticationToken objeto que tiene el nombre de usuario y la contraseña 
	        // authenticate metodo para autenticar al usuario
	        // authentica al usuario utilizando las credenciales
	     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
	        
	        // se obtiene el userdetail y se genera un token
	        System.out.println("hola" + request.getUsername());
	        User user = userRepository.findByUsername(request.getUsername())
	                .orElseThrow(() -> new RuntimeException("No se pudo encontrar el usuario con el nombre de usuario: " + request.getUsername()));
	       
	        CustomUserDetails userCustom=new CustomUserDetails(user);	
	        
	        // genera un token 
	        String token = jwtService.getToken(userCustom);
	        
	        // devuelve el objeto token creado
	        return authResponse.builder().token(token).build();
	    } catch (Exception e) {
	        // Captura cualquier excepción que ocurra durante el proceso y muestra un mensaje de error
	        System.out.println("Error durante el inicio de sesión: " + e.getMessage());
	        // Puedes manejar la excepción de otra manera, como lanzarla nuevamente o devolver un mensaje de error específico
	        return authResponse.builder().error("Error durante el inicio de sesión").build();
	    }
	}

	public authResponse register(RegisterRequest request) {
		   // Verifica que todos los campos necesarios estén presentes en la solicitud
	    if (request.getUsername() == null || request.getPassword() == null || request.getFirstname() == null ||
	            request.getLastname() == null || request.getCountry() == null || request.getRole()==null) {
	        // Maneja el caso en el que falta algún campo necesario
	        return authResponse.builder().error("Falta información necesaria para el registro").build();
	    }
		
		// Encripta la contraseña proporcionada en la solicitud
      //  String encodedPassword = passwordEncoder.encode(request.getPassword());
		//patron builder para la construccion de objetos
		//construye un usuario
	    System.out.println(request.getRole());
	    User user = null;
	    if(request.getRole().equals("USER")) {
	    user = User.builder()
                .username(request.getUsername())  // Establece el username desde la solicitud
                .password(passwordEncoder.encode(request.getPassword()))       // Establece la contraseña encriptada
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)
                .build();
		 userRepository.save(user);

	    }else if(request.getRole().equals("ADMIN")) {
	    	
	       user = User.builder()
	                .username(request.getUsername())  // Establece el username desde la solicitud
	                .password(passwordEncoder.encode(request.getPassword()))       // Establece la contraseña encriptada
	                .firstname(request.getFirstname())
	                .lastname(request.getLastname())
	                .country(request.getCountry())
	                .role(Role.ADMIN)
	                .build();
			 userRepository.save(user);

	    	
	    }else {
	    	
	        throw new IllegalArgumentException("Invalid role: " + request.getRole());
	    }
		     
		 CustomUserDetails userDetails = new CustomUserDetails(user);
		// Genera un token utilizando el objeto CustomUserDetails
		    String token = jwtService.getToken(userDetails);
			System.out.println("user"+user.toString());
			System.out.println(userRepository.listar());
		
		
		return authResponse.builder().token((token)).build();
		}

	}
