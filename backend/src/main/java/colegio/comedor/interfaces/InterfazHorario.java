package colegio.comedor.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import colegio.comedor.modelo.Estudiante;
import colegio.comedor.modelo.Horario;

@Repository
public interface InterfazHorario extends  CrudRepository<Horario,String> {

}
