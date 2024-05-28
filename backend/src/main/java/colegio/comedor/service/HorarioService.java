package colegio.comedor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaceService.IHorarioService;
import colegio.comedor.interfaces.InterfazHorario;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Asistencia;
import colegio.comedor.modelo.Horario;

@Service
public class HorarioService implements IHorarioService{
	@Autowired
	private InterfazHorario dataHorario;
	
	
	
	
	@Override
	public List<Horario> listar() {
		// TODO Auto-generated method stub
		return (List<Horario>)dataHorario.findAll();
	}

	@Override
	public Optional<Horario> listarId(String id) {
		// TODO Auto-generated method stub
		return dataHorario.findById(id);
	}

	@Override
	public Horario save(Horario e) {
		dataHorario.save(e);
		return e;
		
	}

	@Override
	public void delete(String id) {
		dataHorario.deleteById(id);
		
	}

	@Override
	public Optional<Horario> findByUsername(String username) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Horario> buscarUsuario(double id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public  Optional<Horario> findById(Integer id) {
		String valor=id.toString();
		// TODO Auto-generated method stub
		return dataHorario.findById(valor);
	}

	

}
