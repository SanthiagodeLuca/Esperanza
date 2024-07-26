package colegio.comedor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import colegio.comedor.modelo.Asistencia;
import colegio.comedor.modelo.Estudiante;
import colegio.comedor.service.AsistenciaService;
import colegio.comedor.service.EstudianteService;
import colegio.comedor.service.ExcelService;
import jakarta.annotation.Resource;

import org.springframework.ui.Model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;


@RestController
@RequestMapping("/api/excel") 
@CrossOrigin(origins= {"http://localhost:4200"})
public class controladorExcel {

	@Autowired
    private ExcelService excelService;
	
	@Autowired
	private EstudianteService operacionEstudiante;
	
	@Autowired
	private AsistenciaService serviceAsistencia;
	
	private Sheet hoja;
	
	
	@GetMapping("/mostrarExcel")
	public String mostrarExcel() {
		
		
		return "excel";
	}
	
	@PostMapping("/subirExcel")
	public ResponseEntity<String> manejadorSubidaArchivos(@RequestParam("file") MultipartFile file, Model modelo) throws IOException {
	    String[] requiredHeaders = {
	        "NRO_DOCUMENTO", "APELLIDO1", "APELLIDO2", "NOMBRE1", "NOMBRE2",
	        "DESCRIP_JORNADA", "GRUPO", "ESPECIAL"
	    };

	    try (InputStream is = file.getInputStream()) {
	        hoja = excelService.procesarArchivo(file);
	        Row headerRow = hoja.getRow(0);
	        for (int i = 0; i < requiredHeaders.length; i++) {
	            if (headerRow.getCell(i) == null || !headerRow.getCell(i).getStringCellValue().equals(requiredHeaders[i])) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("El archivo no tiene el formato correcto.");
	            }
	        }

	        insertarDatos(modelo);
	        return ResponseEntity.ok().body("{\"message\": \"Archivo subido correctamente\"}");
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Error de lectura del archivo: " + e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Archivo no correspondiente: " + e.getMessage());
	    }
	}

	@GetMapping("/insertarDatos")
	public ResponseEntity insertarDatos(Model modelo) {
	    Estudiante estudiante = new Estudiante();
	    boolean primeraLinea = true;
	    for (Row fila : hoja) {
	        if (primeraLinea) {
	            primeraLinea = false;
	            continue;
	        }
	       
	        for (int i = 0; i < fila.getPhysicalNumberOfCells(); i++) {
	            Cell celda = fila.getCell(i);
	            if (celda != null) {
	                String valorCelda = "";
	                // Convertir el valor de la celda a cadena de texto
	                if (celda.getCellType() == CellType.STRING) {
	                    valorCelda = celda.getStringCellValue();
	                } else if (celda.getCellType() == CellType.NUMERIC) {
	                    // Si es numérico, convertirlo a cadena de texto
	                    valorCelda = String.valueOf(celda.getNumericCellValue());
	                }
	                // Procesar la celda según su posición en la fila
	           
	                if (i == 0) {
	                    String estudianteId = valorCelda;
	                    Estudiante existenteEstudiante = operacionEstudiante.listarId(estudianteId).orElse(null);
	                    if (existenteEstudiante != null) {
	                        continue;
	                    }
	                    estudiante.setId(estudianteId);
	                } else if (i == 3) {
	                    estudiante.setNombre(valorCelda);
	                } else if (i == 5) {
	                    estudiante.setJornada(valorCelda);
	                } else if (i == 6) {
	                    estudiante.setCurso(valorCelda);
	                } else if(i == 7) {
	                    // Si es booleano, parsearlo como cadena y luego a booleano
	                    estudiante.setEspecial(Boolean.parseBoolean(valorCelda));
	                }
	            }
	        }
	        if (estudiante.getId() != null) {
	        	System.out.println("Nuevo estudiante");
	            operacionEstudiante.save(estudiante);
	            operacionEstudiante.imagenQR(estudiante);
	            estudiante = new Estudiante(); // Restablecer el objeto estudiante para el siguiente ciclo
	        }
	    }
	    return ResponseEntity.ok("Datos insertados correctamente.");
	}


	
	@GetMapping("/obtenerModelo")
	public String otraVista(Model modelo) {
	   
	    return "codigoQR";
	}
	@GetMapping("/export")
	public ResponseEntity<ByteArrayResource> exportExcel(
	        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
	        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) throws IOException {
	    // Obtener asistencias entre las fechas especificadas
	    List<Asistencia> asistencias = serviceAsistencia.obtenerFechasAsistencia(startDate, endDate);
	    
	    // Generar archivo Excel con las asistencias obtenidas
	    byte[] excelBytes = excelService.generateExcelFile(asistencias);
	    
	    // Preparar las cabeceras HTTP
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=asistencias.xlsx");
	    headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

	    // Construir y retornar la respuesta
	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(new ByteArrayResource(excelBytes));
	}
	/*@GetMapping("/export")
	public ResponseEntity<ByteArrayResource> exportExcel() throws IOException {

		
			//representa al archivo en bytes del excel
		 byte[] excelBytes = excelService.generateExcelFile(); // Llamar al método que genera el archivo Excel en bytes
		 //prepara cabeceras http
		 HttpHeaders headers = new HttpHeaders();
	     //indica al navegador que debe tratar al contenido de  la cabecera como un archivo conjunto  
		 headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe.xlsx");
		 //indica el tipo de archivo
	        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//construyec una respuesta con un estado 200
	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(new ByteArrayResource(excelBytes));
    }*/
	
	
	@PostMapping("/actualizarDatosInforme")
	public ResponseEntity<String> actualizarDatosInforme(@RequestBody List<List<String>> datos) {
	    try {
	        // Aquí deberías procesar y actualizar el archivo Excel con los datos recibidos
	        // Usar Apache POI o cualquier otra librería que prefieras
	        // Guardar los cambios
	    	
	    	excelService.actualizarExcel(datos);
	        
	        return ResponseEntity.ok("Datos actualizados correctamente.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar los datos.");
	    }
	}

	
	
	
	
}
