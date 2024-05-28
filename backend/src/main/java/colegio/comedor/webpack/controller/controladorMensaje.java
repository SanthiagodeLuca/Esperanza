package colegio.comedor.webpack.controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins= {"http://localhost:4200"})

public class controladorMensaje {
	   @MessageMapping("/message")
	    @SendTo("/topic/messages")
	    public String handleMessage(@Payload String message) {
	        // Procesar el mensaje recibido
	        return "Mensaje recibido: " + message;
	    }
}
