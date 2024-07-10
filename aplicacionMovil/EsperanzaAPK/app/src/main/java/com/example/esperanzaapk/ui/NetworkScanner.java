package com.example.esperanzaapk.ui;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkScanner {

    private static final String TAG = "NetworkScanner";
    private static final int TIMEOUT = 1000; // 1 second timeout
    private static final String FILE_NAME = "server_ip.txt";
    private Context context;
    private Handler mainHandler;

    public NetworkScanner(Context context, Handler mainHandler) {
        this.context = context;
        this.mainHandler = mainHandler;
    }

    public void scanNetwork(Runnable onScanComplete) {
        if (!isConnectedToWifi()) {
            Toast.makeText(context, "Por favor, conéctese a una red WiFi", Toast.LENGTH_SHORT).show();
            return;
        }

        ExecutorService executor = Executors.newFixedThreadPool(255);
        CountDownLatch latch = new CountDownLatch(254);
        AtomicBoolean ipSaved = new AtomicBoolean(false);

        // Obtener la subred actual en la que está conectado el dispositivo
        String currentSubnet = getCurrentSubnet();

        for (int i = 1; i < 255; i++) {
            final int lastOctet = i;
            executor.execute(() -> {
                try {
                    String ip = currentSubnet + lastOctet;
                    InetAddress inetAddress = InetAddress.getByName(ip);
                    if (inetAddress.isReachable(TIMEOUT)) {
                        if (checkHttpServer(ip)) {
                            saveIp(ip);
                            ipSaved.set(true);
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
                    if (ipSaved.get()) {
                        Toast.makeText(context, "Se actualizó la IP correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "No se pudo actualizar la IP", Toast.LENGTH_SHORT).show();
                    }
                    onScanComplete.run();
                });
            } catch (InterruptedException e) {
                Log.e(TAG, "Error esperando a que terminen los hilos", e);
            }
        }).start();
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
        }
        return false;
    }

    private void saveIp(String ip) {
        File file = new File(context.getExternalFilesDir(null), FILE_NAME);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(ip.getBytes());
            Log.d(TAG, "IP saved: " + ip);
        } catch (IOException e) {
            Log.e(TAG, "Error saving IP", e);
        }
    }

    private boolean isConnectedToWifi() {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
