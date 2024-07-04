package colegio.comedor.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.ActualizadorImagenes;
import colegio.comedor.PowerShellExecutor;
import colegio.comedor.interfaceService.IAsistenciaService;
import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.modelo.Estudiante;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor//asigna al contructor valores 
@CrossOrigin(origins= {"http://localhost:4200"})
public class controladorEstudiante {
	@Autowired
	private IEstudianteService serviceEstudiante;
	
	@Autowired
	private IAsistenciaService serviceAsistencia;
	private static final Logger logger = LoggerFactory.getLogger(controladorEstudiante.class);
	
	@PostMapping(value="/bienvenida")
	public String welcome() {

		
	      return "welcome works";
	}
	
	
	@GetMapping("/")
	public String home() {
		PowerShellExecutor.ejecutarScript();  
		return "Home";
	}
	@GetMapping("/listar")
	public String listar(Model modelo) {
		
		List<Estudiante>estudiantes=serviceEstudiante.listar();
		modelo.addAttribute("estudiantes",estudiantes);
		modelo.addAttribute("miTablaId", "miTablaEstudiantes");
		Map<String, Object> atributos = modelo.asMap();
	   
	    // Itera sobre los atributos y los imprime en la consola
	    atributos.forEach((key, value) -> System.out.println(key + " : " + value));
		
		
		return "index";
		
	}
	 @GetMapping("/mostrarQR")
	    public String mostrarQR(Model model) {
	        // Agrega la ruta del código QR al modelo
		 List<Estudiante>listado=serviceEstudiante.listar();
		 List<String> rutas = new ArrayList<>();
		 
		 for(Estudiante e:listado) {
		 boolean exito = rutas.add("/images/"+""+e.getId()+".png");
		 e.setImagen("/images/"+""+e.getId()+".png");
		// System.out.println("ruta imagen"+e.getImagen());
		 }
		 System.out.println("Rutas generadas correctamente: " +rutas.toString());
		 model.addAttribute("rutas",rutas);
		 model.addAttribute("estudiantes", listado);
		 
		 
	     model.addAttribute("miTablaQR","tablaQR");

		 
	        // Devuelve el nombre de la vista (archivo HTML)
	        return "codigoQR";
	    }
	@GetMapping("/new")
	public String agregar(Model modelo) {
		
		modelo.addAttribute("estudiante",new Estudiante());
		return "formulario";
	}
	@PostMapping("/save")
	public String save(@Validated Estudiante e) {
		
		serviceEstudiante.save(e);
		serviceEstudiante.imagenQR(e);
		return "redirect:/listar";
	} 
	@PostMapping("/agregar")
	public ResponseEntity agregarEstudiante(@Valid @RequestBody Estudiante nuevoEstudiante){
		
		
		serviceEstudiante.save(nuevoEstudiante);
		serviceEstudiante.imagenQR(nuevoEstudiante);
		ActualizadorImagenes.actualizarImagenes(nuevoEstudiante.getId());
		
        return new ResponseEntity<>(nuevoEstudiante, HttpStatus.CREATED);
	}
	//ver que ocurre con esto se supone que no funcionaria
	@PutMapping("/cambiarEstudiante/{id}")
	public ResponseEntity editar(@PathVariable String id,@RequestBody Estudiante estudiante) {
	
		Optional<Estudiante>estudianteOpt=serviceEstudiante.listarId(id);
		  if (estudianteOpt.isPresent()) {
	            Estudiante estudianteActualizar = estudianteOpt.get();
	        //    estudianteActualizar.setId(estudiante.getId());
	            estudianteActualizar.setImagen(estudiante.getImagen());
	            estudianteActualizar.setJornada(estudiante.getJornada());
	            estudianteActualizar.setNombre(estudiante.getNombre());
	            
	            // Guardar la instancia actualizada
	            serviceEstudiante.save(estudianteActualizar);
	            logger.info("editando asistencia: " + estudiante);

	            return ResponseEntity.ok(estudianteActualizar);
	        } else {
	            // Manejar el caso en que la asistencia no fue encontrada
	            return ResponseEntity.notFound().build();
	        
	}
		  }
	
	
	
	@DeleteMapping("/eliminarEstudiante/{id}")
	public ResponseEntity eliminarEstudiante(@PathVariable String id) {
		
		serviceEstudiante.delete(id);
		 serviceEstudiante.eliminarQR(id);
		
		
		return ResponseEntity.ok("chill");
	}
	@GetMapping("/eliminar/{id}")
	public String eliminar(Model modelo,@PathVariable String id) {
		
		 serviceEstudiante.delete(id);
		 serviceEstudiante.eliminarQR(id);
		 return "redirect:/listar";
	}
	
	@GetMapping("/limpiar")
	public String limpiarEstudiantes(Model model) {
		serviceEstudiante.limpiar();
		
		
		return "redirect:/listar";
	}
	
	
	//maneja todas las solicitudes GET y lo devuelve la lsta con http al cliente
	@GetMapping
    public ResponseEntity<List<Estudiante>> getEstudiantes() {
        List<Estudiante> estudiantes = serviceEstudiante.listar();
        return ResponseEntity.ok(estudiantes);
    }
	
	
	
	
}
