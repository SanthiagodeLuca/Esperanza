package colegio.comedor.webpack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import colegio.comedor.modelo.Mensaje;

@Controller
//@CrossOrigin(origins= {"http://localhost:4200"})

public class controladorWebSocket {
/*	   @MessageMapping("/nuevaAsistencia")
	    @SendTo("/topic/asistencias")
	    public Asistencia nuevaAsistencia(Asistencia asistencia) {
	        // Aquí puedes procesar la nueva asistencia y guardarla en la base de datos si es necesario
	        // serviceAsistencia.save(asistencia);
	        return asistencia;
	    }	
	@MessageMapping("/chat.register")
	@SendTo("/topic")
	public Mensaje register(@Payload Mensaje mensaje,SimpMessageHeaderAccessor headerAccesor) {
		
		headerAccesor.getSessionAttributes().put("username", mensaje.getEnviar());
		return mensaje;
	}
	
	@MessageMapping("/chat.send")
	@SendTo("/topic/public")
	public Mensaje sendMessage(@Payload Mensaje mensaje) {
		
		
		return mensaje;
	}
	*/
	 @Autowired
	    SimpMessagingTemplate simpMessagingTemplate;
	 
	 //los mensajes /app/application seran manejasdos por este metodo
	  @MessageMapping("/application")
	  //define donde se enviaran todos los mensajes 
	  //se enviaran a todos los subcriptores de /all/messages
	    @SendTo("/all/messages")
	    public Message send(final Message message) throws Exception {
	        return message;
	    }
}
