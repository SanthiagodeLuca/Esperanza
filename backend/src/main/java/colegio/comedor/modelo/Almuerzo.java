package colegio.comedor.modelo;



import java.util.HashSet;
import java.util.Set;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="almuerzo")
public class Almuerzo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String nombre;
    
    @ManyToMany(mappedBy = "almuerzo")
    private Set<Asistencia> asistencia = new HashSet<>();

    
   
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Asistencia> getAsistencia() {
		return asistencia;
	}

	public void setAsistencia(Set<Asistencia> asistencia) {
		this.asistencia = asistencia;
	}
    
    // Getters y Setters
    
}
