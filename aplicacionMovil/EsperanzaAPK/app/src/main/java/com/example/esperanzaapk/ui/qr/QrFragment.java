package com.example.esperanzaapk.ui.qr;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.esperanzaapk.databinding.FragmentQrBinding;
import com.example.esperanzaapk.ui.login.ApiService;
import com.example.esperanzaapk.ui.login.RetrofitClient;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QrFragment extends Fragment {

    private FragmentQrBinding binding; // Objeto que contiene referencias a las vistas del layout fragment_qr.xml
    private DecoratedBarcodeView barcodeView; // Vista personalizada para escanear códigos de barras y QR
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100; // Código de solicitud para el permiso de cámara
    private boolean qrRead = false; // Bandera para controlar si se ha leído un código QR recientemente
    private ApiService apiService; // Interfaz para realizar llamadas HTTP utilizando Retrofit
    private Handler handler; // Manejador para programar tareas para el escáner de códigos QR
    private Runnable scannerRunnable; // Tarea que se ejecutará después de un tiempo específico para reactivar el escáner
    private ImageView imageCheck; // Imagen de marca de verificación verde para mostrar después de un registro exitoso
    private ImageView imageCross; // Imagen de marca de equis roja para mostrar después de un registro fallido

    private static final String FILE_NAME = "server_ip.txt";
    private static final int DISPLAY_TIME_MS = 4000; // Tiempo de espera en milisegundos (5 segundos)


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Infla y vincula el layout fragment_qr.xml al objeto binding
        binding = FragmentQrBinding.inflate(inflater, container, false);

        // Obtiene la vista raíz del layout inflado
        View root = binding.getRoot();

        // Asigna la vista DecoratedBarcodeView del layout a la variable barcodeView
        barcodeView = binding.barcodeScanner;

        // Asigna las imágenes imageCheck e imageCross del layout a las variables correspondientes
        imageCheck = binding.imageCheck;
        imageCross = binding.imageCross;

        // Verifica si se tiene permiso para utilizar la cámara
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Si no se tiene permiso, solicita permiso al usuario
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Si se tiene permiso, inicia el escáner de códigos QR
            startBarcodeScanner();
        }

        // Retorna la vista raíz del fragmento que contiene el layout inflado
        return root;
    }


    private void startBarcodeScanner() {
        // Utiliza el método decodeSingle para escanear un único código de barras
        barcodeView.decodeSingle(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                // Verifica si el resultado del escaneo no es nulo y si el QR no ha sido leído previamente
                if (result.getText() != null && !qrRead) {
                    qrRead = true; // Marca que el QR ha sido leído para detener la lectura

                    // Obtiene y actualiza el TextView textQr del layout con el contenido del QR escaneado
                    //final TextView textView = binding.textQr;
                    //textView.setText(result.getText());

                    // Procesa el contenido del QR escaneado
                    procesarCodigoQR(result.getText());

                    // Crea un nuevo Handler para manejar el temporizador
                    handler = new Handler();
                    // Define un Runnable para reiniciar el escáner después de 5 segundos
                    scannerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            qrRead = false; // Permite que se lea otro QR
                            startBarcodeScanner(); // Reactiva el escáner nuevamente
                        }
                    };
                    handler.postDelayed(scannerRunnable, DISPLAY_TIME_MS ); // Programa la ejecución después de 5000 milisegundos (5 segundos)
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // Puedes manejar posibles puntos de resultado aquí si es necesario
            }
        });
    }


    private void procesarCodigoQR(String qrContent) {

        String savedIp = readServerIpFromFile();

        Log.d(TAG, "Using IP: " + savedIp); // Verifica qué IP se está usando

        if (savedIp == null) {
            Toast.makeText(requireContext(), "IP no encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el token almacenado en SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Configurar OkHttpClient con un interceptor para agregar el token JWT a las peticiones
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AuthInterceptor(token));

        // Configurar Retrofit para realizar llamadas HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + savedIp + ":8085/api/asistencias/") // Ajustar según la URL base de la API
                .addConverterFactory(GsonConverterFactory.create()) // Convertir respuestas JSON usando Gson
                .client(httpClient.build()) // Usar el cliente OkHttpClient configurado
                .build();

        // Crear una instancia de ApiService para realizar las llamadas definidas en la interfaz
        apiService = retrofit.create(ApiService.class);

        // Obtener la fecha y hora actual en el formato deseado
        String fechaActual = getCurrentDateTime();

        // Log para verificar la fecha y hora que se enviará
        Log.d(TAG, "Fecha y hora a enviar: " + fechaActual);

        // Construir el cuerpo de la solicitud en formato JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", "138"); // ID específico para la solicitud
            jsonBody.put("estudiante", qrContent); // Contenido del QR escaneado, ajustar según la estructura del QR
            jsonBody.put("almuerzo", "1"); // Ejemplo de parámetro adicional
            jsonBody.put("fecha", fechaActual); // Ejemplo de fecha y hora "2024-07-10 1:16:00"
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear el cuerpo de la solicitud HTTP en formato JSON usando RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        // Realizar la solicitud HTTP POST asincrónica con Retrofit
        Call<Void> call = apiService.registrarCodigoQR("Bearer " + token, requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Mostrar un Toast indicando que el registro fue exitoso
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    showCheckMark(); // Mostrar la marca de verificación verde
                } else {
                    // Mostrar un Toast con el mensaje de error específico
                    String errorMessage = "Error al registrar. ";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    /*// Mostrar el mensaje de error en un toast
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("RequestError", errorMessage); // Registrar el mensaje de error en el Log
                    showCrossMark(); // Mostrar la marca de cruz roja*/

                    // Mostrar el mensaje de error en el TextView con id text_qr
                    //TextView textView = binding.textQr;
                    //textView.setText(errorMessage);
                    //Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("RequestError", errorMessage); // Registrar el mensaje de error en el Log
                    showCrossMark(); // Mostrar la marca de cruz roja
                    showAlert(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Mostrar un Toast indicando el error de red
                String errorMessage = "Error de red: " + t.getMessage();
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("NetworkError", errorMessage); // Registrar el error de red en el Log
                showCrossMark(); // Mostrar la marca de cruz roja
                showAlert(errorMessage);
            }
        });
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Método para obtener la fecha y hora actual en el formato deseado y usando la zona horaria del dispositivo
    private String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault()); // Usar la zona horaria del dispositivo
        return dateFormat.format(calendar.getTime());
    }

    private void showCheckMark() {
        // Hacer visible la imagen de marca de verificación
        imageCheck.setVisibility(View.VISIBLE);

        // Animación para hacer aparecer la imagen con una duración de 1000 milisegundos (1 segundo)
        imageCheck.animate().alpha(1f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                // Desvanecer la imagen después de 5 segundos
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Animación para desvanecer la imagen con una duración de 1000 milisegundos (1 segundo)
                        imageCheck.animate().alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Hacer invisible la imagen de marca de verificación al finalizar la animación
                                imageCheck.setVisibility(View.GONE);
                            }
                        }).start();
                    }
                }, DISPLAY_TIME_MS/2 ); // 5000 milisegundos = 5 segundos
            }
        }).start(); // Iniciar la animación de aparición de la imagen
    }

    private String readServerIpFromFile() {
        File file = new File(requireActivity().getExternalFilesDir(null), FILE_NAME);
        if (file.exists()) {
            try {
                byte[] data = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();
                return new String(data, "UTF-8").trim(); // Trim para eliminar espacios en blanco al inicio o final
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null; // O manejar de acuerdo a tu lógica si no se encuentra el archivo
    }




    private void showCrossMark() {
        // Hacer visible la imagen de marca de equis roja
        imageCross.setVisibility(View.VISIBLE);

        // Animación para hacer aparecer la imagen con una duración de 1000 milisegundos (1 segundo)
        imageCross.animate().alpha(1f).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                // Desvanecer la imagen después de 5 segundos
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Animación para desvanecer la imagen con una duración de 1000 milisegundos (1 segundo)
                        imageCross.animate().alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                // Hacer invisible la imagen de marca de equis roja al finalizar la animación
                                imageCross.setVisibility(View.GONE);
                            }
                        }).start();
                    }
                }, DISPLAY_TIME_MS/2 ); // 5000 milisegundos = 5 segundos
            }
        }).start(); // Iniciar la animación de aparición de la imagen
    }


    @Override
    public void onResume() {
        super.onResume();
        // Verificar si se ha otorgado el permiso de cámara
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Detener y reiniciar el escáner
            barcodeView.pause();
            barcodeView.resume();
            startBarcodeScanner(); // Reinicia el escáner
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Pausar la vista de escáner de código de barras
        barcodeView.pause();

        // Detener el temporizador si está activo
        if (handler != null && scannerRunnable != null) {
            handler.removeCallbacks(scannerRunnable);
            handler = null;
            scannerRunnable = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Liberar la referencia al binding para evitar memory leaks
        binding = null;

        // Limpiar el handler y el runnable para evitar memory leaks
        if (handler != null && scannerRunnable != null) {
            handler.removeCallbacks(scannerRunnable);
            handler = null;
            scannerRunnable = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Verificar si el requestCode corresponde al permiso de cámara
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // Verificar si se otorgó el permiso
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Iniciar el escáner de código de barras
                startBarcodeScanner();
            } else {
                // El permiso fue denegado, puedes manejar este caso y mostrar un mensaje al usuario
            }
        }
    }

}
