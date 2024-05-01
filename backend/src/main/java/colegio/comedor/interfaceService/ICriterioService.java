package colegio.comedor.interfaceService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import colegio.comedor.modelo.Criterio;

public interface ICriterioService {
	public List<Criterio>listarCriterio();
	public Optional<Criterio>listarIdCriterios(int id);
	public Criterio save(Criterio e);
	public void delete(int id);
	public void limpiar();
}
