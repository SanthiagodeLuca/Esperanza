	package colegio.comedor.jwt;
	
	import java.io.IOException;
	
	import org.springframework.stereotype.Component;
	import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import colegio.comedor.service.JwtService;
	import jakarta.servlet.FilterChain;
	import jakarta.servlet.ServletException;
	import jakarta.servlet.http.HttpServletRequest;
	import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

	import org.springframework.util.StringUtils;
	@Component	
	//OncePerRequestFilter es la base para los filtros que se ejecutan una vez por solicitud
	//impide que los filtros se apliquen varias veces 
	@RequiredArgsConstructor
	public class authenticationFilter extends OncePerRequestFilter {
	
		private final JwtService jwtservice;
		private final UserDetailsService userDetailService;
		
		//filterChain cadena de filtro
		
		//servlet programa
		//metodo de la interfaz OncePerRequestFilter
		//Este metodo es el responsable de la solicitud entrante 
		@Override
		protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
						final String token=getTokenFromRequest(request);
						final String username;
						
						
						
						if(token==null) {
							System.out.println("token nulo");
							//se pasa si no se encuentra el token al siguiente filtro
							filterChain.doFilter(request,response);
							//se devuelve al codigo ya con el toen 
							return;
						}
						
						System.out.println("token no nulo");

						username=jwtservice.getUsernameFromToken(token);
						if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						    UserDetails userDetails = userDetailService.loadUserByUsername(username);
						    System.out.println(jwtservice.isTokenValid(token, userDetails));
						    if (jwtservice.isTokenValid(token, userDetails)) {
						        // Asegúrate de que las autoridades no sean nulas ni estén vacías
						       
						            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						                    userDetails, null, userDetails.getAuthorities());
						            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						            // Establece el objeto UsernamePasswordAuthenticationToken en el contexto de seguridad
						            SecurityContextHolder.getContext().setAuthentication(authToken);
						        
						    }
						}

						
						
						filterChain.doFilter(request,response);
		}
		
	//lo que hace en obtener el token del encabezado de la peticion
	
		private String getTokenFromRequest(HttpServletRequest request) {
			// TODO Auto-generated method stub
			//tratar de encontrar en el header
			final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
			
			//ACCCEDEMOS AL ENCABEZADO
			//Authorization: Bearer <token_JWT> asi se veria en el encabezado
	
				//extraer el token
			//StringUtils.hasText(authHeader) verifica que la cadena no sea nula
			//verifica que el encabezado contenga Bearer	
			if(StringUtils.hasText(authHeader)&&authHeader.startsWith("Bearer")) {
				
				//devulve aqui el token 
				//quita el bearer del encabezado para obtener solo el token
				return authHeader.substring(7);
			}
			
			
			return null;
		}
	
		//OncePerRequestFilter  permite crear filtros
		
		
	}
