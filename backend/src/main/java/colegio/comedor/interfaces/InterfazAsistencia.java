package colegio.comedor.interfaces;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import colegio.comedor.modelo.Asistencia;

@Repository
public interface InterfazAsistencia extends CrudRepository<Asistencia,Integer> {

	  List<Asistencia> findByFechaBetween(Date startDate, Date endDate); // Change to 'fecha'
	
	// public List<Asistencia> findByFechaAsistenciaBetween(Date startDate, Date endDate);
}
