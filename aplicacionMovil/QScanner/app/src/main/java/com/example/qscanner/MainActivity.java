package com.example.qscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.DateIntervalFormat;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button scan_btn;
    TextView textView;

    TextView texto;
    ApiService apiService;
    String ipAddress;
    private String obtenerDireccionIpPorNombre(String nombreEquipo) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            // Obtiene la lista de dispositivos conectados a la misma red Wi-Fi
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader("/proc/net/arp"));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 4) {
                        String device = parts[0];
                        String ip = parts[0]; // La segunda columna suele contener la dirección IP
                        if (device.equals(nombreEquipo)) {
                            return ip;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }
    private boolean esIPv4(String ipAddress) {
        String[] partes = ipAddress.split("\\.");
        if (partes.length != 4) {
            return false;
        }

        for (String parte : partes) {
            try {
                int valor = Integer.parseInt(parte);
                if (valor < 0 || valor > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    private void obtenerTodasLasDireccionesIp() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            // Obtiene la lista de dispositivos conectados a la misma red Wi-Fi
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader("/proc/net/arp"));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] partes = line.split("\\s+");
                    if (partes.length >= 4) {
                        String ipAddress = partes[0]; // La segunda columna suele contener la dirección IP

                        // Verifica si es una dirección IPv4
                        if (esIPv4(ipAddress)) {
                            Log.d("Dirección IP", ipAddress);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate ejecutado");
        setContentView(R.layout.activity_main);

        scan_btn = findViewById(R.id.scanner);
        textView = findViewById(R.id.text);

        //String nombreEquipo = "DESKTOP-V2FO19F";
        // InetAddress direccion = null;
       /* try {
            direccion = InetAddress.getByName(nombreEquipo);
            Log.e("la direccion es",direccion.getAddress().toString());

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String referenciaIp = direccion.getHostAddress();
*/


        //obtenerTodasLasDireccionesIp();
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //configuracion del scanner
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

/*
        WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        texto = findViewById(R.id.text);


        texto.setText(Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));
*/
        String direccionIP = getIntent().getStringExtra("DIRECCION_IP");
        // Configura el tiempo de espera personalizado
      /*  OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(300000, TimeUnit.MILLISECONDS) // 5 minutos en milisegundos
                .readTimeout(300000, TimeUnit.MILLISECONDS);*/
        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl("http://"+direccionIP+":8085/") // URL base del servicio
               // .baseUrl("http://192.168.1.100:8085/")
                .addConverterFactory(GsonConverterFactory.create()) // Utiliza Gson para convertir JSON
                .build();

        // Crear una instancia del servicio
        apiService = retrofit.create(ApiService.class);
    // texto = findViewById(R.id.text);


     //    texto.setText(direccionIP);

}

        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            String content = intentResult.getContents();
           // JSONObject jsonData = null;
            if (content != null) {
                textView.setText(content);
                if (apiService != null) {
                    JsonObject datos = new JsonObject();
                    String s = "1000329482";
                    datos.addProperty("estudiante", content);
                    // Hacer la solicitud
                    Call<ResponseBody> call = apiService.enviarDatos(datos);
                    Log.d("MainActivity", "Antes de hacer la solicitud");
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            int statusCode = response.code();
                            Log.d("Response Code", String.valueOf(statusCode));

                            if (response.isSuccessful()) {
                                // La solicitud fue exitosa, puedes procesar la respuesta si hay alguna
                                Log.d("MainActivity", "Solicitud exitosa");
                            } else {
                                // La solicitud no fue exitosa, puedes manejar el error aquí
                                Log.d("MainActivity", "Error en la solicitud");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("MainActivity", "Error en la solicitud", t);
                        }


                    });
                }else {
                    // Manejar el caso en el que apiService es nulo
                    Log.e("MainActivity", "apiService es nulo");
                }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}}