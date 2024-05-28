package colegio.comedor.deserializer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import colegio.comedor.interfaceService.IAlmuerzoService;
import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.modelo.Almuerzo;
import colegio.comedor.modelo.Estudiante;

public class AlmuerzoDeserializer extends JsonDeserializer<Almuerzo> {
	
	   @Autowired
	    private IAlmuerzoService almuerzoService;

	@Override
	public Almuerzo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		 String almuerzoId = p.getText();

		return almuerzoService.listarId(Integer.parseInt(almuerzoId)).orElse(null);
	}

}
