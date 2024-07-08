package colegio.comedor.interfaceService;

import java.util.List;
import java.util.Optional;

import colegio.comedor.modelo.Estudiante;

public interface IEstudianteService {
	public List<Estudiante>listar();
	public Optional<Estudiante> listarId(String id);
	public int save(Estudiante e);
	public void delete(String id);
	public int imagenQR(Estudiante e);
	public void eliminarQR(String id);	
	public void limpiar();
	public Long contarEstudiantes();
	//Estudiante buscarEstudiantePorNombre(String nombre);

}
