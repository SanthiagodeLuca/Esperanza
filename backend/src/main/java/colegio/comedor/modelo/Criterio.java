package colegio.comedor.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="criterios")
public class Criterio {

	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int id;
	
	private String nombre;
	
	private String tipoCriterio;

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

	public String getTipoCriterio() {
		return tipoCriterio;
	}

	public void setTipoCriterio(String tipoCriterio) {
		this.tipoCriterio = tipoCriterio;
	}
	
	
		
		
}
