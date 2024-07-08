package colegio.comedor.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import colegio.comedor.GeneradorQR;
import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.interfaces.InterfazEstudiante;
import colegio.comedor.modelo.Estudiante;

@Service
public class EstudianteService implements IEstudianteService {
	
	@Autowired
	private InterfazEstudiante data;
	
	
	@Override
	public List<Estudiante> listar() {
		// TODO Auto-generated method stub
		return (List<Estudiante>)data.findAll();
	}

	public Optional<Estudiante> listarId(String id) {

		
		return data.findById(id);
	}

	@Override
	public int save(Estudiante e) {
		int res=0;
		Estudiante estudiante=data.save(e);
		if(!estudiante.equals(null)) {
			res=1;

		}
		return res;
	}

	@Override
	public void delete(String id) {
		data.deleteById(id);
	}
	
	@Override
	public int imagenQR(Estudiante e) {
		
		if(!e.equals(null)) {
	
		GeneradorQR.generarCodigoQR(""+e.getId());
		return 1;
		}
		return 0;
		
	}
	public void eliminarQR(String id) {
		
		
		String ruta= "src/main/resources/static/images/" + id + ".png";
		
		File archivo=new File(ruta);
		if (archivo.exists()) {
            // Intenta eliminar el archivo
            if (archivo.delete()) {
                System.out.println("Archivo eliminado: " + archivo.getName());
            } else {
                System.out.println("No se pudo eliminar el archivo: " + archivo.getName());
            }
        } else {
            System.out.println("El archivo no existe en la ruta proporcionada.");
        }
		
		
		
		
		
	}

	@Override
	public void limpiar() {
		// TODO Auto-generated method stub
		data.deleteAll();
		}
/*
	@Override
	public Estudiante buscarEstudiantePorNombre(String nombre) {
		// TODO Auto-generated method stub
		//return data.findByid(nombre);
	/*}*/

	@Override
	public Long contarEstudiantes() {
		// TODO Auto-generated method stub
		return data.count();
	}
	
	
	
}
