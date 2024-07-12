package com.example.esperanzaapk.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.esperanzaapk.MainActivity;
import com.example.esperanzaapk.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView loginStatusTextView;
    private TextView ipDisplayTextView;
    private Button loginButton;
    private Button editIpButton;
    private SharedPreferences sharedPreferences;

    private static final String FILE_NAME = "server_ip.txt";
    private static final String TAG = "LoginActivity";
    private static final int TIMEOUT = 2000; // 2 second timeout

    private Context context;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        mainHandler = new Handler(Looper.getMainLooper());

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginStatusTextView = findViewById(R.id.login_status);
        loginButton = findViewById(R.id.login_button);
        editIpButton = findViewById(R.id.edit_ip_button);
        ipDisplayTextView = findViewById(R.id.ip_display);

        displaySavedIp();

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            login(username, password);
        });

        editIpButton.setOnClickListener(v -> showEditIpDialog());
    }

    private void showEditIpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_ip, null);
        builder.setView(dialogView);

        final EditText ipEditText = dialogView.findViewById(R.id.ip_edit_text);
        final TextView ipSavedDisplay = dialogView.findViewById(R.id.ip_saved_display);
        String savedIp = getSavedIp();
        ipSavedDisplay.setText("IP guardada: " + savedIp);

        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();

        Button automaticButton = dialogView.findViewById(R.id.automatic_button);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button closeButton = dialogView.findViewById(R.id.close_button);

        automaticButton.setOnClickListener(view -> {
            if (!isConnectedToWifi()) {
                Toast.makeText(context, "Por favor, conéctese a una red WiFi", Toast.LENGTH_SHORT).show();
                return;
            }

            ExecutorService executor = Executors.newFixedThreadPool(255);
            CountDownLatch latch = new CountDownLatch(254);
            AtomicBoolean ipSavedFlag = new AtomicBoolean(false);

            // Obtener la subred actual en la que está conectado el dispositivo
            String currentSubnet = getCurrentSubnet();

            for (int i = 1; i < 255; i++) {
                final int lastOctet = i;

                executor.execute(() -> {
                    try {
                        String ip = currentSubnet + lastOctet;
                        InetAddress inetAddress = InetAddress.getByName(ip);
                        if (inetAddress.isReachable(TIMEOUT)) {
                            Log.d("IPActive", "IP: " + lastOctet);
                            if (checkHttpServer(ip)) {
                                saveIp(ip);
                                ipSavedFlag.set(true);
                            }
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Error checking IP", e);
                    } finally {
                        latch.countDown(); // Contador de hilos completados
                    }
                });
            }

            // Hilo para esperar a que todos los hilos finalicen antes de mostrar el Toast
            new Thread(() -> {
                try {
                    latch.await(); // Espera hasta que todos los hilos terminen
                    // Cuando todos los hilos terminan, muestra el Toast y oculta el ProgressBar
                    mainHandler.post(() -> {
                        if (ipSavedFlag.get()) {
                            Toast.makeText(context, "Se actualizó la IP correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "No se pudo actualizar la IP", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        displaySavedIp();
                    });
                } catch (InterruptedException e) {
                    Log.e(TAG, "Error esperando a que terminen los hilos", e);
                }
            }).start();
        });

        saveButton.setOnClickListener(view -> {
            String ip = ipEditText.getText().toString();
            if (ip.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, ingrese una IP", Toast.LENGTH_SHORT).show();
                return;
            }
            saveIp(ip); // Guardar la IP ingresada
            dialog.dismiss();
            displaySavedIp();
        });

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void login(String username, String password) {
        String savedIp = getSavedIp();
        Log.d(TAG, "Using IP: " + savedIp); // Verifica qué IP se está usando

        String baseUrl = "http://" + savedIp + ":8085/auth/";

        ApiService apiService = RetrofitClient.getClient(baseUrl).create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, password);

        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getToken() != null && !loginResponse.getToken().isEmpty()) {
                        // Guardar el token en SharedPreferences
                        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", loginResponse.getToken());
                        editor.apply();

                        // Redirigir a MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // Finalizar LoginActivity
                    } else {
                        loginStatusTextView.setText(loginResponse.getError() != null ? loginResponse.getError() : "Error desconocido");
                    }
                } else {
                    loginStatusTextView.setText("Error en la respuesta del servidor");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginStatusTextView.setText("Error de red: " + t.getMessage());

                // Intentar reiniciar la instancia de Retrofit en caso de error
                RetrofitClient.invalidateClient();
            }
        });
    }


    private void displaySavedIp() {
        File file = new File(getExternalFilesDir(null), FILE_NAME);
        if (file.exists()) {
            try {
                byte[] data = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();
                String savedIp = new String(data, "UTF-8");
                runOnUiThread(() -> ipDisplayTextView.setText("IP: " + savedIp));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            runOnUiThread(() -> ipDisplayTextView.setText("IP: No IP saved"));
        }
    }

    private void saveIp(String ip) {
        File file = new File(getExternalFilesDir(null), FILE_NAME);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(ip.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSavedIp() {
        File file = new File(getExternalFilesDir(null), FILE_NAME);
        if (file.exists()) {
            try {
                byte[] data = new byte[(int) file.length()];
                FileInputStream fis = new FileInputStream(file);
                fis.read(data);
                fis.close();
                return new String(data, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "No IP saved";
    }

    private String getCurrentSubnet() {
        // Obtener la dirección IP del dispositivo actual en la red WiFi
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ipString = Formatter.formatIpAddress(ipAddress);

        // Determinar la subred quitando el último octeto de la IP
        String[] octets = ipString.split("\\.");
        return octets[0] + "." + octets[1] + "." + octets[2] + ".";
    }

    private boolean checkHttpServer(String ip) {
        try {
            URL url = new URL("http://" + ip + ":8085/auth/check"); // Ajusta la ruta URL según sea necesario
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setReadTimeout(TIMEOUT);
            if (urlConnection.getResponseCode() == 200) {
                return true;
            }
            urlConnection.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "Error checking HTTP server", e);
        }
        return false;
    }

    private boolean isConnectedToWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
