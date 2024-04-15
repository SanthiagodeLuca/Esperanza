package colegio.comedor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class MiClase {
	 
	
	@Value("${horaInicialAlmuerzo.variable}")
	private int horaAlmueroVariable;
	
	@Value("${horaFinalAlmuerzo.variable}")
	private int horaFinalAlmuerzoVariable;
	
	@Value("${minutoInicialAlmuerzo.variable}")
	private int minutoInicialAlmuerzoVariable;
	
	@Value("${minutoFinalAlmuerzo.variable}")
	private int minutoFinalAlmuerzoVariable;
	
	@Value("${horaInicialDesayuno.variable}")
	private int horaInicialDesayunoVariable;
	
	@Value("${horaFinalDesayuno.variable}")
	private int horaFinalDesayunoVariable;
	
	@Value("${minutoInicialDesayuno.variable}")
	private int minutoInicialDesayunoVariable;
	
	@Value("${minutoFinalDesayuno.variable}")
	private int minutoFinalDesayunoVariable;
	
	
	@Value("${actualizar.variable}")
	private String actualizar;
	
	
	
	
	
	public String getActualizar() {
		return actualizar;
	}

	public void setActualizar(String actualizar) {
		this.actualizar = actualizar;
	}

	public int getHoraAlmueroVariable() {
		return horaAlmueroVariable;
	}

	public void setHoraAlmueroVariable(int horaAlmueroVariable) {
		this.horaAlmueroVariable = horaAlmueroVariable;
	}

	public int getHoraFinalAlmuerzoVariable() {
		return horaFinalAlmuerzoVariable;
	}

	public void setHoraFinalAlmuerzoVariable(int horaFinalAlmuerzoVariable) {
		this.horaFinalAlmuerzoVariable = horaFinalAlmuerzoVariable;
	}

	public int getMinutoInicialAlmuerzoVariable() {
		return minutoInicialAlmuerzoVariable;
	}

	public void setMinutoInicialAlmuerzoVariable(int minutoInicialAlmuerzoVariable) {
		this.minutoInicialAlmuerzoVariable = minutoInicialAlmuerzoVariable;
	}

	public int getMinutoFinalAlmuerzoVariable() {
		return minutoFinalAlmuerzoVariable;
	}

	public void setMinutoFinalAlmuerzoVariable(int minutoFinalAlmuerzoVariable) {
		this.minutoFinalAlmuerzoVariable = minutoFinalAlmuerzoVariable;
	}

	public int getHoraInicialDesayunoVariable() {
		return horaInicialDesayunoVariable;
	}

	public void setHoraInicialDesayunoVariable(int horaInicialDesayunoVariable) {
		this.horaInicialDesayunoVariable = horaInicialDesayunoVariable;
	}

	public int getHoraFinalDesayunoVariable() {
		return horaFinalDesayunoVariable;
	}

	public void setHoraFinalDesayunoVariable(int horaFinalDesayunoVariable) {
		this.horaFinalDesayunoVariable = horaFinalDesayunoVariable;
	}

	public int getMinutoInicialDesayunoVariable() {
		return minutoInicialDesayunoVariable;
	}

	public void setMinutoInicialDesayunoVariable(int minutoInicialDesayunoVariable) {
		this.minutoInicialDesayunoVariable = minutoInicialDesayunoVariable;
	}

	public int getMinutoFinalDesayunoVariable() {
		return minutoFinalDesayunoVariable;
	}

	public void setMinutoFinalDesayunoVariable(int minutoFinalDesayunoVariable) {
		this.minutoFinalDesayunoVariable = minutoFinalDesayunoVariable;
	}


	@Value("${provicional.variable}")
	private String provicionalVariable;

    public String getProvicionalVariable() {
		return provicionalVariable;
	}

	public void setProvicionalVariable(String provicionalVariable) {
		this.provicionalVariable = provicionalVariable;
	}


	@Value("${mi.variable}")
    private String miVariable;

    public String getMiVariable() {
        return miVariable;
    }

	public void setMiVariable(String miVariable) {
		this.miVariable = miVariable;
	}
	
	
	@Value("${error.variable}")
	private String errorVariable;

	public String getErrorVariable() {
		return errorVariable;
	}

	public void setErrorVariable(String errorVariable) {
		this.errorVariable = errorVariable;
	}
	
	@Value("${mientras.variable}")
	private String mientrasVariable;

	public String getMientrasVariable() {
		return mientrasVariable;
	}

	public void setMientrasVariable(String mientrasVariable) {
		this.mientrasVariable = mientrasVariable;
	}
	
	
	
}