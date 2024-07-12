package com.example.esperanzaapk.ui.login;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("nuevaAsistencia")
    Call<Void> registrarCodigoQR(
            @Header("Authorization") String authToken,
            @Body RequestBody requestBody
    );

}

