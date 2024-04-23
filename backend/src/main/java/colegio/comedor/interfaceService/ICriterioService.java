package colegio.comedor.interfaceService;


import java.util.List;

import java.util.Optional;


import colegio.comedor.modelo.Criterio;

public interface ICriterioService {
	public List<Criterio>listar();
	public Optional<Criterio>listarId(int id);
	public int save(Criterio e);
	public void delete(int id);

	public void limpiar();
}
