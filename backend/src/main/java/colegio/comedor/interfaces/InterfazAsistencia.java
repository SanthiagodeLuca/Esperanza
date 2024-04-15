package colegio.comedor.interfaces;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import colegio.comedor.modelo.Asistencia;

@Repository
public interface InterfazAsistencia extends CrudRepository<Asistencia,Integer> {

}
