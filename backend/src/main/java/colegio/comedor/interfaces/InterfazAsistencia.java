package colegio.comedor.interfaces;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import colegio.comedor.modelo.Asistencia;

@Repository
public interface InterfazAsistencia extends CrudRepository<Asistencia,Integer> {

	  List<Asistencia> findByFechaBetween(Date startDate, Date endDate); // Change to 'fecha'


	  @Query("SELECT a FROM Asistencia a WHERE a.estudiante.id = :estudianteId AND a.fecha BETWEEN :fechaStart AND :fechaEnd AND a.almuerzo.id = :tipoComida")
	  Optional<Asistencia> buscarAsistenciaExistente(@Param("estudianteId") String estudianteId, @Param("fechaStart") Date fechaStart, @Param("fechaEnd") Date fechaEnd, @Param("tipoComida") int tipoComida);


	// public List<Asistencia> findByFechaAsistenciaBetween(Date startDate, Date endDate);
}
