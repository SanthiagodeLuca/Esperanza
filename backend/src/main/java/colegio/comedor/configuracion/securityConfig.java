package colegio.comedor.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import colegio.comedor.jwt.authenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.Collections;

import javax.ws.rs.core.HttpHeaders;

import lombok.RequiredArgsConstructor;

//FILTROS
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class securityConfig {
	//endpoints protegido y otros que no 
	
	//filtro de autenticacion 
	private final authenticationFilter jwtAuthenticationFilter;
	//proveedor de autenticacion
	private final AuthenticationProvider authProvider;
	
	
	//configura la cadena de filtros
	//httpSecurity configura la seguridad de las solicitudes
	//define quien puede acceder a recursos 
	//que acciones estan permitidas 
	//como se manejan las sesiones
	
	//este bean produce un objeto relacionado con con la seguridad
	//sprinsecurity accede a este objeto y lo utiliza para configurar la seguridad 
	//de la aplicacion
	//el objeto que devuelve es de tipo 
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		
		
		
		//todos los auth se permite a todos solicitarlo
		return http.csrf(csrf->csrf.disable())//desabilita la proteccion CRSF
				.authorizeHttpRequests(authRequest ->
				authRequest
				.requestMatchers(HttpMethod.GET).permitAll()
				.requestMatchers(HttpMethod.POST).permitAll()
				.requestMatchers("/auth/**").permitAll()//Permite el accesto a /auth/**  los dos puntos dicen que cualquier cosa que se ponga asi se puede acceder osea subdirectorios auth/login, /auth/register, /auth/reset- 
					.anyRequest().authenticated())//para otras solicitudes require autenticacion
				.sessionManagement(
								
								sessionManager->sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)//politica de creacion de sesion
			
								)
				.authenticationProvider(authProvider)//configura un proveedor de autentificacion
				//dentro de spring security el container de los fiter ejecuta los filtros sin necesidad de se llamados			
				.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)//b Agrega el filtro de autenticación JWT antes del filtro de autenticación por nombre de usuario y contraseña
				.cors()
				.and()
		        .build();//finaliza la contruccion de un objeto 
						}
	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.addAllowedOrigin("http://localhost:4200");
	        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
	        configuration.setAllowCredentials(false);
	        configuration.setAllowedHeaders(Arrays.asList(

	        		
	        		HttpHeaders.AUTHORIZATION,
	        		HttpHeaders.CONTENT_TYPE,
	        		HttpHeaders.ACCEPT
	        		
	        		));

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }

	    @Bean
	    public CorsFilter corsFilter() {
	        return new CorsFilter(corsConfigurationSource());
	    }

}
