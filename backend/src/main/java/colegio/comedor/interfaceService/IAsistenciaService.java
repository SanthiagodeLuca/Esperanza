package colegio.comedor.interfaceService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.ui.Model;

import colegio.comedor.modelo.Asistencia;

public interface IAsistenciaService {
	public List<Asistencia>listar();
	public Optional<Asistencia>listarId(int id);
	public int save(Asistencia e);
	public void delete(int id);
	public Date guardarFecha(int hora,int minuto);
	public int guardarAsistenciaQR(Map<String,String> contenidoQR);
	public void limpiar();
}
