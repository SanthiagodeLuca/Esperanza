package com.example.qscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionActivity extends AppCompatActivity {

    EditText editTextDireccionIP;
    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        editTextDireccionIP = findViewById(R.id.editTextDireccionIP);
        btnContinuar = findViewById(R.id.btnContinuar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String direccionIP = editTextDireccionIP.getText().toString();

                // Pasar la dirección IP a MainActivity
                //Intent intent = new Intent(ConfiguracionActivity.this, MainActivity.class);
                Intent intent = new Intent(ConfiguracionActivity.this, com.example.qscanner.MainActivity.class);
                intent.putExtra("DIRECCION_IP", direccionIP);
                startActivity(intent);
            }
        });
    }
}
