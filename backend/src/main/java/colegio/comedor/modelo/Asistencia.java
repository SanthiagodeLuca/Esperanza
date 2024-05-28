package colegio.comedor.modelo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import colegio.comedor.deserializer.AlmuerzoDeserializer;
import colegio.comedor.deserializer.CustomDeserializer;
import colegio.comedor.deserializer.EstudianteDeserializer;

import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="asistencia")
public class Asistencia {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    @JsonDeserialize(using = EstudianteDeserializer.class)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "almuerzos_id")
    @JsonDeserialize(using = AlmuerzoDeserializer.class)
    private Almuerzo almuerzo;
    
    @JsonDeserialize(using = CustomDeserializer.class)
    private Date fecha;
    
    // Resto de atributos y Getters/Setters

   
    
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Estudiante getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}

	public Almuerzo getAlmuerzo() {
		return almuerzo;
	}

	public void setAlmuerzo(Almuerzo almuerzo) {
		this.almuerzo = almuerzo;
	}
    
}

