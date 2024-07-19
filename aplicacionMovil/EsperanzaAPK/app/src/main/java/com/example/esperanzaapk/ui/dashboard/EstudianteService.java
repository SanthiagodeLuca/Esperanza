package com.example.esperanzaapk.ui.dashboard;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EstudianteService {
    @GET("estudiantes/total")
    Call<Integer> obtenerTotalEstudiantes();
}