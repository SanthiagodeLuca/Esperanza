	package colegio.comedor.service;
	
	import java.sql.SQLException;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.dao.DataIntegrityViolationException;
	import org.springframework.jdbc.core.JdbcTemplate;
	import org.springframework.stereotype.Service;
	
	@Service
	public class BaseDatosService {
	
	    @Autowired
	    private JdbcTemplate jdbcTemplate;
	
	    // ...
	/*
	    public void insertarEnBaseDeDatos() {
	        try {
	            // Tu código para insertar en la base de datos aquí
	        } catch (DataIntegrityViolationException e) {
	            if (e.getCause() instanceof SQLException) {
	                SQLException sqlException = (SQLException) e.getCause();
	                if (sqlException.getErrorCode() == 1644) {
	                    // Código específico para el error 1644 aquí
	                    // Puedes ejecutar tu función y recuperar la fila que no se pudo insertar.
	                	System.out.println("funciona");
	                }
	            }
	        }
	    }
	}*/
	}