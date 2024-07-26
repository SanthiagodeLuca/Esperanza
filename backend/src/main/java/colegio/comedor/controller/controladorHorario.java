package colegio.comedor.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import colegio.comedor.interfaceService.IHorarioService;
import colegio.comedor.modelo.Horario;

@RestController
@RequestMapping("/api/horario") 
@CrossOrigin(origins= {"http://localhost:4200"})
public class controladorHorario {

    @Autowired
    private IHorarioService horarioService;
    

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarHorario(@PathVariable Integer id,@RequestBody Horario nuevoHorario) {
    	
    	 Optional<Horario>  horarioExistente = Optional.ofNullable(horarioService.findById(id).orElseThrow(()->new RuntimeException("Horario no encontrado")));
   
    	 Horario horarioActualizado  = new Horario();
    	 if(horarioExistente!=null) {
        // Actualizar los datos del horario existente con los nuevos datos
        horarioExistente.get().setStartHour(nuevoHorario.getStartHour());
        horarioExistente.get().setStartMinute(nuevoHorario.getStartMinute());
        horarioExistente.get().setEndHour(nuevoHorario.getEndHour());
        horarioExistente.get().setEndMinute(nuevoHorario.getEndMinute());

        horarioExistente.get().setTipoComida(nuevoHorario.getTipoComida());


        // Guardar el horario actualizado en la base de datos
         horarioActualizado  = horarioService.save(horarioExistente.get());
    }else {
    	
    	
    	horarioExistente = Optional.empty();
        horarioExistente.get().setId(1); // Establece el ID para mantener la consistencia
        horarioExistente.get().setStartHour(nuevoHorario.getStartHour());
        horarioExistente.get().setStartMinute(nuevoHorario.getStartMinute());
        horarioExistente.get().setEndHour(nuevoHorario.getEndHour());
        horarioExistente.get().setEndMinute(nuevoHorario.getEndMinute());
          horarioActualizado  = horarioService.save(horarioExistente.get());
    }
    
    
    
        return ResponseEntity.ok().body(horarioActualizado);
    }
    @GetMapping("/refrigerio/{id}")
    public ResponseEntity<?> obtenerRefrigerio(@PathVariable Integer id) {
        Optional<Horario> refrigerio = horarioService.findById(id);
        if (refrigerio.isPresent()) {
            return ResponseEntity.ok().body(refrigerio.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/desayuno/{id}")
    public ResponseEntity<?> obtenerDesayuno(@PathVariable Integer id) {
        Optional<Horario> desayuno = horarioService.findById(id);
        if (desayuno.isPresent()) {
            return ResponseEntity.ok().body(desayuno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @GetMapping("/almuerzo/{id}")
    public ResponseEntity<?> obtenerAlmuerzo(@PathVariable Integer id) {
        Optional<Horario> desayuno = horarioService.findById(id);
        if (desayuno.isPresent()) {
            return ResponseEntity.ok().body(desayuno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    }
