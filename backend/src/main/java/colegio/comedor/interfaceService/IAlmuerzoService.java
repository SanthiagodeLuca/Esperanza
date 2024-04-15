package colegio.comedor.interfaceService;

import java.util.List;
import java.util.Optional;

import colegio.comedor.modelo.Almuerzo;

public interface IAlmuerzoService {
	public List<Almuerzo>listar();
	public Optional<Almuerzo>listarId(int id);
	public int save(Almuerzo e);
	public void delete(int id);
}
