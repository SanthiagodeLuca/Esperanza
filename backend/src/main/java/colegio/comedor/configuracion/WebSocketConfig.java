		package colegio.comedor.configuracion;
		
		import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
		
		
	
		@Configuration
		@EnableWebSocketMessageBroker
	@CrossOrigin(origins= {"http://localhost:4200"})

		public class WebSocketConfig implements  WebSocketMessageBrokerConfigurer {
			/*
			 @Bean
			    public WebMvcConfigurer corsConfigurer() {
			        return new WebMvcConfigurer() {
			            @Override
			            public void addCorsMappings(CorsRegistry registry) {
			                registry.addMapping("/ws/**")
			                        .allowedOrigins("http://localhost:4200")
			                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
			            }
			        };
			    }
			 @Bean
			    public SimpUserRegistry userRegistry() {
			        return new DefaultSimpUserRegistry();
			    }
			 @Bean
			    public MessageChannel messageChannel() {
			        return new DirectChannel();
			    }
	
			    @MessagingGateway(defaultRequestChannel = "messageChannel")
			    public interface WebSocketGateway {
			        void send(String message);
			    }
	
			    @ServiceActivator(inputChannel = "messageChannel")
			    public void handle(String message) {
			        // Aquí puedes manejar los mensajes recibidos en el canal 'application.messageChannel'
			    }
			    
			 @Bean
			    public SimpMessagingTemplate messagingTemplate(MessageChannel messageChannel, SimpUserRegistry userRegistry) {
			        return new SimpMessagingTemplate(messageChannel);
			    }
			    */	
		
		
		    //establece un punto de conexion(punto donde dos sistemas pueden comunicarse entre si)
		    //"ws://ejemplo.com/socket". el cliente trata de conectar a la url ,y cuando se establece se pueden comunicar
		    //sockjs permite al navegador que no usan websocket usar websocket
			 // Configura el Message Broker para manejar la mensajería dentro de la aplicación
		    @Override
		    public void configureMessageBroker(MessageBrokerRegistry config) {
		        // Habilita un simple broker de mensajes en las rutas especificadas ("/topic", "/all", "/specific")
		        // Estos son los destinos donde los clientes pueden suscribirse para recibir mensajes
		        config.enableSimpleBroker("/topic", "/all", "/specific");
		        
		        // Define un prefijo para las rutas de destino de la aplicación
		        // Los mensajes enviados desde los clientes que empiecen con "/app" se enrutarán a los métodos
		        // anotados con @MessageMapping en los controladores
		        config.setApplicationDestinationPrefixes("/app");
		    }

		    // Registra los puntos de conexión STOMP donde los clientes pueden conectar
		    @Override
		    public void registerStompEndpoints(StompEndpointRegistry registry) {
		        // Agrega un endpoint STOMP en la ruta "/ws"
		        // Los clientes se conectarán a este endpoint para iniciar la comunicación WebSocket
		        registry.addEndpoint("/ws")
		                // Permite solicitudes desde el origen "http://localhost:4200"
		                // Esto es necesario para permitir la comunicación entre el frontend (Angular) y el backend
		                .setAllowedOrigins("http://localhost:4200")
		                // Habilita el soporte de SockJS para navegadores que no admiten WebSocket nativo
		                .withSockJS();
		    }
}