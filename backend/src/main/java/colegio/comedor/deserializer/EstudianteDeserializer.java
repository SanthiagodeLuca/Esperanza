package colegio.comedor.deserializer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import colegio.comedor.interfaceService.IEstudianteService;
import colegio.comedor.modelo.Estudiante;

public class EstudianteDeserializer extends JsonDeserializer<Estudiante>  {
    @Autowired
    private IEstudianteService estudianteService;

    @Override
    public Estudiante deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String estudianteId = p.getText();
        return estudianteService.listarId(estudianteId).orElse(null);
    }


}
