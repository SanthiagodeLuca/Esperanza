package colegio.comedor.interfaces;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import colegio.comedor.modelo.Estudiante;
@Repository
public interface InterfazEstudiante extends CrudRepository<Estudiante,String> {
   // Optional<Estudiante> findById(String id);
}
