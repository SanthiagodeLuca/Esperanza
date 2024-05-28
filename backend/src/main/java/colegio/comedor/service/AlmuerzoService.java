package colegio.comedor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaces.IntefazAlmuerzo;
import colegio.comedor.interfaces.InterfazAsistencia;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Asistencia;
@Service
public class AlmuerzoService implements IAlmuerzoService {

	

	@Autowired
	private IntefazAlmuerzo dataAlmuerzo;
	@Override
	public List<Almuerzo> listar() {
		// TODO Auto-generated method stub
		return (List<Almuerzo>)dataAlmuerzo.findAll();
	}

	@Override
	public Optional<Almuerzo> listarId(int id) {
		// TODO Auto-generated method stub
		return dataAlmuerzo.findById(id);

	}

	@Override
	public int save(Almuerzo e) {
		// TODO Auto-generated method stub
		int res=0;
		Almuerzo estudiante=dataAlmuerzo.save(e);
		if(!estudiante.equals(null)) {
			res=1;

		}
		return res;
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		dataAlmuerzo.deleteById(id);
	}

}
