package colegio.comedor.interfaces;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import colegio.comedor.modelo.Criterio;

@Repository
public interface InterfazCriterio extends  CrudRepository<Criterio,Integer>{

}
