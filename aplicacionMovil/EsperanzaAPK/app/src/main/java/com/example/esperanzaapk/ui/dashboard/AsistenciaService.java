package com.example.esperanzaapk.ui.dashboard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AsistenciaService {
    @POST("asistencias/fecha")
    Call<List<Asistencia>> obtenerAsistencias(@Body FilterAsistencia filter);
}