package colegio.comedor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import colegio.comedor.interfaceService.ICriterioService;
import colegio.comedor.interfaces.InterfazCriterio;
import colegio.comedor.modelo.Asistencia;
import colegio.comedor.modelo.Criterio;
import colegio.comedor.modelo.Estudiante;

@Service
public class criterioService implements ICriterioService {

	
		@Autowired 
		private InterfazCriterio dataCriterio;
		
		@Override
		public List<Criterio> listarCriterio() {
			// TODO Auto-generated method stub
			return (List<Criterio>)dataCriterio.findAll();
		}

		@Override
		public Optional<Criterio> listarIdCriterios(int id) {
			// TODO Auto-generated method stub
			return dataCriterio.findById((long) id);
		}

		@Override
		public Criterio save(Criterio e) {
			// TODO Auto-generated method stub
		
			return dataCriterio.save(e);
		}

		@Override
		public void delete(int id) {
			dataCriterio.deleteById((long) id);
			
		}

		@Override
		public void limpiar() {
			dataCriterio.deleteAll();
		}
}
