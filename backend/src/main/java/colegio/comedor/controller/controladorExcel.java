package colegio.comedor.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import colegio.comedor.modelo.Estudiante;
import colegio.comedor.service.EstudianteService;
import colegio.comedor.service.ExcelService;
import jakarta.annotation.Resource;

import org.springframework.ui.Model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;


@Controller
public class controladorExcel {

	@Autowired
    private ExcelService excelService;
	
	@Autowired
	private EstudianteService operacionEstudiante;
	
	
	private Sheet hoja;
	
	
	@GetMapping("/mostrarExcel")
	public String mostrarExcel() {
		
		
		return "excel";
	}
	
	@PostMapping("/subirExcel")
	public String manejadorSubidaArchivos(@RequestParam("file") MultipartFile file,Model modelo) throws IOException {
		
		hoja=excelService.procesarArchivo(file);
		System.out.println("Hoja procesada correctamente.");
        	
		insertarDatos(modelo);
		
		//modelo.addAttribute("estudiantes", operacionEstudiante.listar()); // Asegúrate de agregar la lista al modelo
		
		return "excel";
		
	}
	@GetMapping("/insertarDatos")
	public String insertarDatos(Model modelo) {
		//List<Estudiante>estudiantes=operacionEstudiante.listar();
		//modelo.addAttribute("estudiantes",estudiantes);
		Estudiante estudiante=new Estudiante();
		boolean primeraLinea = true;
		for(Row fila:hoja) {

			
		    // Resto del código de procesamiento para la fila

		    // ...
			if(primeraLinea) {
				
				primeraLinea=false;
				continue;
			}


		for(int i=0;i<fila.getPhysicalNumberOfCells();i++) {
			
			
		
			//System.out.println("valor de la celda"+fila.getCell(i)+"id :"+i);
			if(i==0) {
				
				estudiante.setId(fila.getCell(i).getStringCellValue());
				//estudiante.setImagen(fila.getCell(i).getStringCellValue());
				//System.out.println("estudiante con id :"+estudiante.getId());
			}
			
			if(i==3) {
				
				estudiante.setNombre(fila.getCell(i).getStringCellValue());
				//System.out.println("estudiante con id :"+estudiante.getId()+"nombre "+estudiante.getNombre());
			}
			
			if(i==5) {
				
				estudiante.setJornada(fila.getCell(i).getStringCellValue());
			//	System.out.println("estudiante con id :"+estudiante.getId()+"nombre "+estudiante.getNombre()+"jornada "+estudiante.getJornada());
			}
			if(i==6) {
				
				estudiante.setCurso(fila.getCell(i).getStringCellValue());
				//System.out.println("esudiante id :"+estudiante.getId()+"nombre :"+estudiante.getNombre()+" jornada :"+estudiante.getJornada()+" curso :"+estudiante.getCurso());
				
				
				
				
				
				//modelo.addAttribute("estudiante", new Estudiante());
				
			//	System.out.println("Estudiante guardado correctamente.");
				
				//modelo.addAttribute("estudiante", new Estudiante());
			}
			
			if(i==7) {
				
				
				estudiante.setEspecial(Boolean.parseBoolean(fila.getCell(i).getStringCellValue()));
				
				operacionEstudiante.save(estudiante);
				operacionEstudiante.imagenQR(estudiante);
			}
			
		}
		
		
	}	
	
		
		
		return "/mostrarQR";
	}
	
	
	@GetMapping("/obtenerModelo")
	public String otraVista(Model modelo) {
	   
	    return "codigoQR";
	}
	
	@GetMapping("/export")
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
    }
	
	
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
