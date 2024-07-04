package colegio.comedor;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ActualizadorImagenes {

    public static void actualizarImagenes(String texto) {
        String directorio = "src/main/resources/static/images/";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directorio))) {
            for (Path entrada : stream) {
                if (Files.isRegularFile(entrada)) {
                    String nombreArchivo = entrada.getFileName().toString();
                    // Verificar si el archivo coincide con el texto del código QR
                    if (nombreArchivo.startsWith(texto)) {
                        // Realizar la lógica de actualización (por ejemplo, renombrar o eliminar)
                        // En este ejemplo, simplemente imprimimos el nombre del archivo
                        System.out.println("Se encontró una imagen correspondiente al código QR: " + nombreArchivo);
                        // Lógica de actualización aquí...
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
