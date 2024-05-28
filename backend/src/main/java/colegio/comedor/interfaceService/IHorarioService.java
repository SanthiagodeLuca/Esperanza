package colegio.comedor.interfaceService;

import java.util.List;
import java.util.Optional;

import colegio.comedor.modelo.Horario;
import colegio.comedor.modelo.User;

public interface IHorarioService {
	public List<Horario>listar();
	public Optional<Horario> listarId(String id);
	public Horario save(Horario e);
	public void delete(String id);
	public Optional<Horario> findByUsername(String username);
	public Optional<Horario> buscarUsuario(double id);
	public Optional<Horario> findById(Integer id);
}
