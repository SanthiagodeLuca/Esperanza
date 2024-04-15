package colegio.comedor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PowerShellExecutor {

	public static void ejecutarScript() {
        try {
        	//tiene el comando 
        	//ExecutionPolicy Bypass permite ejecutar el scripts sin restricciones
            String comando = "powershell.exe -ExecutionPolicy Bypass -File C:/Users/C N T/Documents/habilitarPuerto.ps1";
            
            //crear un proceso en el SO y ejecuta el comando almacenado
            Process proceso = Runtime.getRuntime().exec(comando);
            //leer la salida del proceso
            BufferedReader resultado = new BufferedReader(new InputStreamReader(proceso.getInputStream()));

            String linea;
            while ((linea = resultado.readLine()) != null) {
                System.out.println(linea);
            }

            proceso.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
