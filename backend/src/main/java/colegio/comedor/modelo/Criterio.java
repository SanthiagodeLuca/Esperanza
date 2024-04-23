package colegio.comedor.modelo;

import javax.persistence.Entity;

import javax.persistence.Table;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name="criterios")
public class Criterio {

	  @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private int id;
	  
	  private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	  
}
