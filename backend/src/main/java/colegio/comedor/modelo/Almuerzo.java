package colegio.comedor.modelo;



import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="almuerzo")
public class Almuerzo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String nombre;
    
    @JsonIgnore
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
