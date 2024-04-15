package colegio.comedor.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.apache.poi.ss.usermodel.CellType;

@Service
public class ExcelService {
 
    public Sheet procesarArchivo(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
          Sheet sheet = workbook.getSheetAt(0);
          return sheet;
          
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar el error
        }
        
        
        String nombreArchivo = file.getOriginalFilename();

        // Guardar el archivo en el directorio de recursos
        String rutaDirectorio = "src/main/resources/static/data/";
        Path rutaArchivo = Paths.get(rutaDirectorio + nombreArchivo);
        Files.write(rutaArchivo, file.getBytes());
		return null;
    }
    
    public byte[] generateExcelFile() throws IOException {
        // Cargar la plantilla Excel
        Resource resource = new ClassPathResource("static/data/informe.xlsx");
     //lee contenido del archivo
        InputStream input = resource.getInputStream();

        // Modificar el archivo Excel aquí con los datos necesarios
     // Modificar el archivo Excel aquí con los datos necesarios
        // Por ejemplo, usando una librería como Apache POI

      
        //se crea un flujo de bytes para almacenar los datos 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        //lee el contenido del archivo de entrada input y lo escribe en baos	(flujo de bytes)
        while ((len = input.read(buffer)) > -1) {
        	//se escribe los datos en el baos
            baos.write(buffer, 0, len);
        }
        //asegura que los datos se han escrito en el flujo de datos 
        baos.flush();

        //archivo modificado en formato de arreglo bytes
        return baos.toByteArray();
    }
    
    private static final int COLUMN_TO_SEARCH = 1;
    
    private static final String RUTA= "src/main/resources/static/data/informe.xlsx";

    
    public Row buscarFilaPorValor(String valorABuscar, String rutaArchivo) {
        Row filaEncontrada = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(rutaArchivo));
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row != null && row.getCell(COLUMN_TO_SEARCH) != null
                        && row.getCell(COLUMN_TO_SEARCH).getCellType() == CellType.STRING
                        && row.getCell(COLUMN_TO_SEARCH).getStringCellValue() == valorABuscar) {
                    filaEncontrada = row;
                    break;
                }
            }

            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filaEncontrada;
    }

    public void insertarDatoEnFila(Row fila, int columna, String valor) {
        if (fila != null) {
            Cell cell = fila.createCell(columna);
            cell.setCellValue(valor);
        }
    }

    public void guardarCambios(String rutaArchivo, Workbook workbook) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(rutaArchivo);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void eliminarDatosExcel(String rutaArchivo) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(rutaArchivo));
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Obtener el número total de filas
            int numRows = sheet.getPhysicalNumberOfRows();

            // Iterar sobre las filas desde la última hacia la primera para evitar problemas con los índices
            for (int r = numRows - 1; r >= 0; r--) {
                Row row = sheet.getRow(r);
                if (row != null) {
                    // Eliminar la fila
                    sheet.removeRow(row);
                }
            }

            // Guardar los cambios
            FileOutputStream fileOutputStream = new FileOutputStream(rutaArchivo);
            workbook.write(fileOutputStream);
            fileOutputStream.close();

            System.out.println("Datos eliminados correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al eliminar los datos.");
        }
    }

    
    public void moverFechasAFilaCorrecta() {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/static/data/informe.xlsx"));
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            //recorrer bucle
         // Obtener la fila de encabezados
            Row headerRow = sheet.getRow(0);

            // Iterar sobre las celdas de la fila de encabezados
            for (int c = 0; c < headerRow.getLastCellNum(); c++) {
            	//System.out.println("headerRow "+headerRow.getLastCellNum());
            	//System.out.println("sheet "+sheet.getLastRowNum());
                Cell cell = headerRow.getCell(c);

                if (cell != null) {
                    // Verificar el tipo de dato de la celda
                    switch (cell.getCellType()) {
                        case STRING:
                            String value = cell.getStringCellValue();
                            System.out.println("Valor de la celda: " + value);
                            break;
                        case NUMERIC:
                            double numericValue = cell.getNumericCellValue();
                            System.out.println("Valor numérico de la celda: " + numericValue);
                            break;
                        case BOOLEAN:
                            boolean booleanValue = cell.getBooleanCellValue();
                            System.out.println("Valor booleano de la celda: " + booleanValue);
                            break;
                        case BLANK:
                            System.out.println("La celda está vacía");
                            break;
                        default:
                            System.out.println("Tipo de celda no reconocido");
                    }
                }
            }
            
            // Iterar sobre las filas
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            	 Row header = sheet.getRow(0);
                Row row = sheet.getRow(r);
               
                int numColumnasHeader = (headerRow != null) ? headerRow.getLastCellNum() : 0;
                int numColumnasRow = (row != null) ? row.getLastCellNum() : 0;

                if (numColumnasHeader != numColumnasRow) {
                    // Los números de columnas son diferentes, agregar celdas "No servicio"
                    int diferenciaColumnas = Math.max(numColumnasHeader, numColumnasRow) - Math.min(numColumnasHeader, numColumnasRow);

                    // Agregar celdas "No servicio"
                    for (int i = 0; i < diferenciaColumnas; i++) {
                        Cell cell = row.createCell(numColumnasRow + i);
                        cell.setCellValue("No servicio");
                    }
                  
                }
                
                //System.out.println("columnas de una fila"+row.getLastCellNum());
                if (row != null) {
                    // Obtener el estudiante
                    Cell cellEstudiante = row.getCell(1);
                    String estudiante = (cellEstudiante != null) ? cellEstudiante.getStringCellValue() : "";

                    //recorre celdas
                    String aux="";
                    String segundoAuxiliar="";
                    for(int celda=0;celda<row.getLastCellNum();celda++) {
                    	
                    	
                    	if(celda==3) {
                    		
                    		System.out.println("valor de la fecha"+row.getCell(celda).getStringCellValue().split(" ")[0]);
                    		aux=row.getCell(celda).getStringCellValue(); 
                    		segundoAuxiliar=aux.split(" ")[0];
                    		break;
                    	}
                    	
                    }

                    for(int celda=3;celda<header.getLastCellNum();celda++) {
                    	  Cell cell = headerRow.getCell(celda);
                    	  //System.out.println("cell "+cell.getStringCellValue());
                    	  //System.out.println("segundo auxiliar"+segundoAuxiliar);
                    	  if(segundoAuxiliar.equals(cell.getStringCellValue())) {
                    		  //System.out.println("entro");
                    		  //System.out.println("valor de la fila a la cual estoy cambiando"+row.getCell(celda).getStringCellValue());
                    		  row.getCell(celda).setCellValue(aux);
                    		  
                    		  //System.out.println("valor que quedo"+row.getCell(celda).getStringCellValue());
                    	  }else {
                    		 // System.out.println("cambio valor");
                    		  row.getCell(celda).setCellValue("No entro");
                    		//  System.out.println("valor que cambio"+row.getCell(celda).getStringCellValue());
                    	  }
                    }
                    
                
                }
            }

            // Guardar los cambios
            FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/static/data/informe.xlsx");
            workbook.write(fileOutputStream);
            fileOutputStream.close();

            System.out.println("Fechas movidas a la columna correspondiente correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al mover las fechas a la columna correspondiente.");
        }
    }


    public void actualizarExcel(List<List<String>> datos) {
    	eliminarDatosExcel(RUTA);
    	System.out.println("actulizacion");
        // Aquí deberías procesar y actualizar el archivo Excel con los datos recibidos
        // Usar Apache POI o cualquier otra librería que prefieras
        // Guardar los cambios
    	 try {
    	        // Cargar el archivo Excel existente
    	        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/static/data/informe.xlsx"));
    	        Workbook workbook = new XSSFWorkbook(fileInputStream);

    	        // Obtener la hoja de trabajo (en este ejemplo, asumimos que es la primera hoja)
    	        Sheet sheet = workbook.getSheetAt(0);

    	        // Mapeo de valores
    	        Map<String, String> mapeo = new HashMap<>();
    	      
    	        mapeo.put("Desayuno","D"); // Presente se mapea a Desayuno
    	        mapeo.put("Almuerzo","A"); // Ausente se mapea a Almuerzo
    	        mapeo.put("Ninguno","N"); // No ingreso se mapea a No ingreso // No ingreso se mapea a No ingreso

    	       // System.out.println("datos"+datos);
    	        Set<String> fechasProcesadas = new HashSet<>();
    	        // Iterar sobre los datos y actualizar las celdas correspondientes
    	        
    	        //Posiciones que se guardaran
    	        Set<String> estudiantesProcesados = new HashSet<>();

    	        
    	        Row headerRow = sheet.createRow(0);

	    	   
    	      

                	//desplazar filas
                	//sheet.shiftRows(filaAInsertar, sheet.getLastRowNum(), 1);
                	headerRow = sheet.createRow(0);
                	Cell cell0 = headerRow.createCell(0);
 	    	     cell0.setCellValue("id_asistencia");
 	
 	    	     Cell cell1 = headerRow.createCell(1);
 	    	     cell1.setCellValue("Estudiante");
 	    	     
 	    	     Cell cell2=headerRow.createCell(2);
 	    	     cell2.setCellValue("Tipo");
           
 	    	 // Obtener el índice de la última fila
 	            int ultimaFila = sheet.getLastRowNum();
    	        
    	        for (int i = 0; i < datos.size(); i++) {
    	          List<String> filaDatos = datos.get(i);
    	          
    	          String id_asistencia=filaDatos.get(0);
    	          String estudiante = filaDatos.get(1);
                 // String fecha = filaDatos.get(filaDatos.size() - 2).split(" ")[0];
                  String tipo = filaDatos.get(2);

    	       //   System.out.println("filaDatos"+filaDatos);
    	         // Row row=sheet.createRow(i);
    	          String fecha = filaDatos.get(filaDatos.size() - 3).split(" ")[0]; // Obtener solo la fecha

    	          if (!fechasProcesadas.contains(fecha)) {
      	        	 
    	        	  
  	                fechasProcesadas.add(fecha);
  	                
  	                // Buscar la columna correspondiente a la fecha
  	                int fechaColumna = -1;
  	                headerRow = sheet.getRow(0);
  	                System.out.println("headerRow"+headerRow);
  	                for (int c = 0; c < headerRow.getLastCellNum(); c++) {
  	                    Cell cell = headerRow.getCell(c);
  	                    if (cell != null && cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(fecha)) {
  	                        fechaColumna = c;
  	                        break;
  	                    }
  	                }

  	                if (fechaColumna == -1) {
  	                    // La columna no existe, debes crearla
  	                	
  	                    fechaColumna = headerRow.getLastCellNum();
  	                    System.out.println("fecha Columna"+fechaColumna);
  	                    Cell fechaCell = headerRow.createCell(fechaColumna);
  	                    //System.out.println("fechaCell "+fechaCell.getStringCellValue());
  	                    fechaCell.setCellValue(fecha);
  	                  System.out.println("fechaCell "+fechaCell.getStringCellValue());
  	                }
  	            }
    	          
    	          
    	          Row row=sheet.createRow(ultimaFila+1);
    	          ultimaFila=ultimaFila+1;
    	            for (int j = 0; j < filaDatos.size()-2; j++) {
    	            	
    	            	
    	            	
    	            	
    	            	
    	            	 if (!estudiantesProcesados.contains(estudiante)) {
    	    	               // Si el estudiante ya fue procesado, salta a la siguiente fila
    	    	            
    	                String valor = filaDatos.get(j);
    	                
    	                Cell cell=row.createCell(j);
    	                System.out.println("valor de cada celda de la fila"+valor);
    	                cell.setCellValue(valor);
    	           
    	            	 }else {
    	            		 
    	            		 Row fila=buscarFilaPorValor(estudiante,RUTA);
    	            		 for(int k = 0; k < filaDatos.size()-2; k++) {
    	            			 
    	            			 String valor=filaDatos.get(k);
    	            		 insertarDatoEnFila(fila,fila.getLastCellNum(),valor);
    	            		 
    	            		 }
    	            	 }
    	            	 }
    	            

      	        
    	               /*
    	               
	    	                if (j == filaDatos.size() - 1) {
	    	                    Cell cell = row.createCell(filaDatos.size());
	    	                    cell.setCellValue(valor);
	    	                } 
    	                	 // Verificar si el valor está en el mapeo
    	                    if (mapeo.containsKey(valor)) {
    	                    	 valor = mapeo.get(valor);
    	                        // Encontramos un valor que ya está mapeado, no necesitamos hacer nada especial
    	                    }*/ 
    	         
    	                // Mapear el valor
    	              

    	           
    	            
    	        }
    	        System.out.println("fechas"+fechasProcesadas.toString());

    	        // Guardar los cambios
    	        FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/static/data/informe.xlsx");
    	        workbook.write(fileOutputStream);
    	        
    	        moverFechasAFilaCorrecta();
    	       
    	        
    	        fileOutputStream.close();
    	        
    	   

    	        System.out.println("Datos actualizados correctamente.");
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        System.err.println("Error al actualizar los datos.");
    	    }
            

            
         
      
    }


    
    //Transforma Excel 
    
 

          
}
