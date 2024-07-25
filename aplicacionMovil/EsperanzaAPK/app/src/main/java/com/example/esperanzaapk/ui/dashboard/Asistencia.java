package com.example.esperanzaapk.ui.dashboard;

import java.sql.Date;

public class Asistencia {
    private int id;
    private Estudiante estudiante;
    private Almuerzo almuerzo;
    private String fecha;

    // Getters y setters

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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

