package colegio.comedor;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;


public class GeneradorQR {
	 private static final String CLAVE = "@kcol2&&klamsum=?(@kira)";

	// Método para obtener el hash SHA-256 de un texto
	 public static String encriptarAES(String texto) {
	        try {
	            SecretKeySpec clave = new SecretKeySpec(CLAVE.getBytes(), "AES");
	            Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            cifrador.init(Cipher.ENCRYPT_MODE, clave);
	            byte[] textoEncriptado = cifrador.doFinal(texto.getBytes());
	            return Base64.getEncoder().encodeToString(textoEncriptado);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	 public static String desencriptarAES(String textoEncriptado) {
	        try {
	            SecretKeySpec clave = new SecretKeySpec(CLAVE.getBytes(), "AES");
	            Cipher cifrador = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            cifrador.init(Cipher.DECRYPT_MODE, clave);
	         // Log del texto encriptado que se está desencriptando
	            System.out.println("Texto encriptado recibido para desencriptar: " + textoEncriptado);
	            
	            byte[] textoDesencriptado = cifrador.doFinal(Base64.getDecoder().decode(textoEncriptado));
	            String textoPlano = new String(textoDesencriptado);
	            
	            // Log del texto desencriptado
	            System.out.println("Texto desencriptado: " + textoPlano);
	            
	            return textoPlano;
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("No se desencripto nada");
	            return null;
	        }
	    }
	//texto sera el id
	//nonmbre de archivo sera el nombre

	public static void generarCodigoQR(String texto) {
		  String rutaGuardado = "src/main/resources/static/images/" + texto + ".png";
	        
	        try {
	            BitMatrix bitMatrix = new MultiFormatWriter().encode(encriptarAES(texto), BarcodeFormat.QR_CODE, 300, 300);
	            Path path = FileSystems.getDefault().getPath(rutaGuardado);
	            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	            System.out.println("Ruta completa: " + path.toAbsolutePath().toString());
	            System.out.println("Código QR generado y guardado exitosamente.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	     //   enviarCodigoQR("ruta/de/tu/codigo/" + nombreArchivo + ".png", nombreArchivo + ".png")
		
	}


}
