		package colegio.comedor.configuracion;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.security.authentication.AuthenticationManager;
	import org.springframework.security.authentication.AuthenticationProvider;
	import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
	import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
	import org.springframework.security.core.userdetails.UserDetailsService;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
	import org.springframework.security.crypto.password.PasswordEncoder;
	import org.springframework.security.core.userdetails.UserDetails;
	import colegio.comedor.interfaceService.IUserService;
import colegio.comedor.modelo.User;
import colegio.comedor.service.userService;
import lombok.RequiredArgsConstructor;
	
	@Configuration
	@RequiredArgsConstructor
	//configura varios componentes AuthenticationManager, AuthenticationProvider, 
	//PasswordEncoder y UserDetailsService.
	//esta clase obtiene el acceso al authentication manager 
	//al authentication provider 
	//al desencriptador y
	//a cargar los detalles del usuario en spring security
	public class aplicationConfig {
	
		@Autowired
		private final userService userRepository;

		
		//utiliza el authenticationConfiguration para obtener el authentication manager 
		@Bean
		public AuthenticationManager autheticationManager(AuthenticationConfiguration config) throws Exception{
			return config.getAuthenticationManager();
			
		}
		//se crea un authentication provider
		//se crea un DaoAuthenticationProvider (clase que tiene el metodo para autentificar con )
		@Bean
		public AuthenticationProvider authenticationProvider() {
			
			//DaoAuthenticationProvider clase que compara las credenciales del objeto authentication 
			
			// y las compara con la fuente de datos(userdetailservice)
			DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
			authenticationProvider.setUserDetailsService(userDetailService());
			authenticationProvider.setPasswordEncoder(passwordEncoder());
			
			
			return authenticationProvider;
			
		}
		
		@Bean
		public PasswordEncoder passwordEncoder() {
			// se utiliza para verificar contraseñas
			return new BCryptPasswordEncoder();
		}
		
		//spring security no requiere que este tenga una etiqueta @Bean 
		//spring security Ya sabe que es un bean 
		//interfaz que define un solo metodo 
		//utilizamo una interfaz que tiene un metodo loadUserByUsername()
		/*
		 * */
		//se genera un bean para spring security que devuelve un usuario
		//despues se llama al metodo loadUserByUsername(internamente )
		//que coge el objeto user y lo convierte en userdetail (contiene la informacion del usuario)
		//y eso spring security lo utiliza para hacer la autentificacion	
		  @Bean
		    public UserDetailsService userDetailService() {
		        return username -> {
		            System.out.println("Buscando usuario en la base de datos para el nombre de usuario: " + username);

		            UserDetails user = userRepository.findByUsername(username)
		                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + username));

		            System.out.println("Usuario encontrado: " + user.getUsername());

		            // Si deseas imprimir más detalles del usuario, puedes hacerlo aquí
		            // System.out.println("Detalles del usuario: " + user);

		            return user;
		        };
		    
			
			// Construir un objeto UserDetails con los detalles del usuario encontrado
		  /*  return org.springframework.security.core.userdetails.User.builder()
		            .username(user.getUsername())
		            .password(user.getPassword()) // Debes proporcionar la contraseña en formato encriptado
		            .roles(user.getRoles())
		            .build();*/
		}
		
	}
