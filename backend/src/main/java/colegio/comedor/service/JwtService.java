	package colegio.comedor.service;
	
	import java.security.Key;
	import java.util.Date;
	import java.util.HashMap;
	import java.util.Map;
	import java.util.function.Function;
	
	import org.springframework.security.core.userdetails.UserDetails;
	import org.springframework.stereotype.Service;
	
	import io.jsonwebtoken.Claims;
	import io.jsonwebtoken.JwtParser;
	import io.jsonwebtoken.Jwts;
	import io.jsonwebtoken.SignatureAlgorithm;
	import io.jsonwebtoken.io.Decoders;
	import io.jsonwebtoken.security.Keys;
	
	@Service
	//genera tokens
	
	public class JwtService {
		
		private static final String SECRET_KEY="84415615648911568494151561891561564151515156121";
	//generar Token 
		//toma un userDetail y devuelve un token
		public String getToken(UserDetails user) {
			
			return getToken(new HashMap<>(),user);
		}
		//toma el userdetail y devulve un token
		private String getToken(Map<String,Object>extraClaims,UserDetails user) {
			
			return Jwts.builder()
					//la firma es valor que se agrega para verificar que los datos no hayan sido
					//modificados
					//ese valor se genera con el algoritmo y una llave y ese valors se agrega al token
					.signWith(getKey(),SignatureAlgorithm.HS256)
					//el token se vence en 24 horas
					.setExpiration(new	Date(System.currentTimeMillis()+1000*60*24))
					//fecha en que fue creado el token
					.setIssuedAt(new Date(System.currentTimeMillis()))
					//agrega reclamaciones son pares  de clave - valor que se agregan al token
					/**
					 * "sub" (subject): Identifica al sujeto del token (generalmente el nombre de usuario).
	"exp" (expiration time): Especifica el tiempo de expiración del token.
	"iss" (issuer): Identifica al emisor del token.
	"roles": Lista de roles o permisos del usuario.
					 */
					.setClaims(extraClaims)
					//establece el sujeto del token con el nombre del usuario
					//vincula usuario y token
					.setSubject(user.getUsername())
					.compact()
					;
					
		}
		//creacion de la clave secreta
		private Key getKey() {
			// crear secret Key
			
			byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
	
			//crear una instancia de la secretkey
			return Keys.hmacShaKeyFor(keyBytes);
		}
		
		
		//coge el token y obtiene las reclamaciones del token
		
			public String getUsernameFromToken(String token) {
				
				// extrae el subject de la reclamacion
				//getsubject devuelve el nombre de usuario
			return getClaims(token,Claims::getSubject);
			}
		
		//verificae si el token es valido comparando
		public boolean isTokenValid(String token, UserDetails userDetails) {
			// TODO Auto-generated method stub
			//obtener username del token
			final String username=getUsernameFromToken(token);
			
			
			return (username.equals(userDetails.getUsername()));//&& isTokenExpired(token)
		}
		
		
		
		private Claims getAllClaims(String token) {
			//jws analizador de token
			//devuelve las reclamaciones del token
			return Jwts
					.parser()
					//verifica la clave
					.setSigningKey(getKey())
					.build()
					//analiza el token para permitir acceder al body
					.parseClaimsJws(token)
					.getBody();
		}
		
		//obtiene todas las reclamaciones
		public <T>T getClaims(String token,Function<Claims,T>claimsResolver){
			//obtiene el valor especifico de las reclamaciones
			//todas las reclamaciones del token
			final Claims claims=getAllClaims(token);
			//se encargar de extraer un valor especifico en reclamaciones 	
			return claimsResolver.apply(claims);
		}
		private Date getExpiration(String token) {
			
			return getClaims(token,Claims::getExpiration);
		}
	
		private boolean isTokenExpired(String token ) {
			return getExpiration(token).before(new Date());
		}
	}
	
	
