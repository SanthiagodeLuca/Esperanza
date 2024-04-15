package colegio.comedor.modelo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name="estudiante")
public class Estudiante {
    @Id
    private String id;
    private String nombre;
    private String jornada;
    private String curso;
    private String imagen;
    
    private boolean especial;
    
    
 
public boolean isEspecial() {
		return especial;
	}
	public void setEspecial(boolean especial) {
		this.especial = especial;
	}

@ManyToMany(mappedBy = "estudiante")
    private Set<Asistencia> asistencias = new HashSet<>();
    
    // Getters y Setters

public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getId() {
		return id;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	public void setId(String id) {
		
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getJornada() {
		return jornada;
	}
	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	public Set<Asistencia> getAsistencias() {
		return asistencias;
	}
	public void setAsistencias(Set<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

}
