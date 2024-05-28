package colegio.comedor.modelo;

import java.io.Serializable;

import colegio.comedor.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Horario",uniqueConstraints= {@UniqueConstraint(columnNames= {"id"})})
public class Horario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
 @GeneratedValue
 Integer Id;
 //@Column(nullable=false)
 Integer startHour;
 Integer startMinute;
 Integer endHour;
 Integer endMinute;
 String tipoComida;

 
}
