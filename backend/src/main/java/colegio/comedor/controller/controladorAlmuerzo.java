package colegio.comedor.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Estudiante;

@Controller
@RequestMapping
public class controladorAlmuerzo {

	@Autowired
	private IAlmuerzoService servicioAlmuerzo;
	
	@GetMapping("/almuerzo")
	public String inicio(Model modelo) {
		List<Almuerzo>almuerzos=servicioAlmuerzo.listar();
		modelo.addAttribute("almuerzos",almuerzos);
		return "almuerzo";
		
	}

	@GetMapping("/listarAlmuerzo")
	public String listarAlmuerzo(Model modelo) {
		
		List<Almuerzo>almuerzos=servicioAlmuerzo.listar();
		modelo.addAttribute("almuerzos",almuerzos);
		return "almuerzo";
		
	}
	
	 @GetMapping("/newAlmuerzo")
		public String crear(Model modelo) {
			
			modelo.addAttribute("almuerzo",new Almuerzo());
			return "formularioAlmuerzo";
		}
	 @PostMapping("/guardarAlmuerzo")
	 public String  guardar(@Validated Almuerzo a) {
		 System.out.println("llego aqui");
		 servicioAlmuerzo.save(a);
		 return "redirect:/listarAlmuerzo";
	 }
	 
	 @GetMapping("/cambiarAlmuerzo/{id}")
		public String editar(@PathVariable int id,Model modelo) {
			Optional<Almuerzo>almuerzo=servicioAlmuerzo.listarId(id);
			modelo.addAttribute("almuerzo", almuerzo);
			return "formularioAlmuerzo";
		}
		
		@GetMapping("/eliminarAlmuerzo/{id}")
		public String eliminar(Model modelo,@PathVariable int id) {
			
			 servicioAlmuerzo.delete(id);
			 return "redirect:/listarAlmuerzo";
		}
	 
	 
	 
}
