package colegio.comedor.interfaces;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import colegio.comedor.modelo.Almuerzo;

@Repository
public interface IntefazAlmuerzo extends CrudRepository<Almuerzo,Integer>{

}
