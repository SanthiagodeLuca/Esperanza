package colegio.comedor.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class controladorIp {
	 @GetMapping("/mostrarDireccionIP")
	    public String mostrarDireccionIP(Model model) {
	        String direccionIP = obtenerDireccionIP();
	        model.addAttribute("direccionIP", direccionIP);
	        return "direccionIp";
	    }

	    private String obtenerDireccionIP() {
	        try {
	            return InetAddress.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	            return "Dirección IP no disponible";
	        }
	    }
}
