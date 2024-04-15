package com.example.qscanner;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("generarAsistenciaQR") // Especifica la ruta del servicio
    Call<ResponseBody> enviarDatos(@Body JsonObject datos); // Define el método y el tipo de respuesta esperada
}
