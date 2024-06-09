package colegio.comerdor.filter.modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
//convertir valores a fechas
public class AsistenciaFilter {
	  	@NonNull
	    @Temporal(TemporalType.TIMESTAMP)
		  private Date startDate;
	  	@NonNull
	    @Temporal(TemporalType.TIMESTAMP)
		  private Date endDate;

		  // Getters and setters
	  	public static Map<String, Date> parseJsonToMap(String jsonData) {
	  	  ObjectMapper mapper = new ObjectMapper();
	  	  try {
	  	    // Configure ObjectMapper to handle dates in a specific format (optional)
	  	    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); // Example format

	  	    Map<String, String> map = mapper.readValue(jsonData, Map.class); // Assuming keys are strings

	  	    // Convert string values to Date objects
	  	    Map<String, Date> dateMap = new HashMap<>();
	  	    for (Map.Entry<String, String> entry : map.entrySet()) {
	  	      try {
	  	        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue()); // Example format
	  	        dateMap.put(entry.getKey(), date);
	  	      } catch (ParseException e) {
	  	        // Handle parsing exception for invalid date format
	  	        throw new RuntimeException("Error parsing date: " + entry.getValue() + ", key: " + entry.getKey());
	  	      }
	  	    }
	  	    return dateMap;
	  	  } catch (JsonProcessingException e) {
	  	    // Handle JSON parsing exception
	  	    throw new RuntimeException("Error parsing JSON data: " + e.getMessage());
	  	  }
	  	}

}
