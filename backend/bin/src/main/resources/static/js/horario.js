function enviarDatos() {
    var formData = {
        horaInicialDesayuno: document.getElementById('horaInicialDesayuno').value,
       
        
        horaFinalDesayuno:document.getElementById('horaFinalDesayuno').value,
        minutoInicialDesayuno: document.getElementById('minutoInicialDesayuno').value,
		minutoFinalDesayuno:document.getElementById('minutoFinalDesayuno').value,
		   
		horaInicialAlmuerzo:document.getElementById('horaInicialAlmuerzo').value,
		horaFinalAlmuerzo:document.getElementById('horaFinalAlmuerzo').value,
		
		minutoInicialAlmuerzo:document.getElementById('minutoFinalAlmuerzo').value,
		minutoFinalAlmuerzo:document.getElementById('minutoFinalAlmuerzo').value
		  
    };

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/horario", // Cambia la URL a donde desees enviar los datos
        data: JSON.stringify(formData),
        dataType: 'json',
        success: function (result) {
            console.log("Datos enviados correctamente", result);
        },
        error: function (e) {
            console.error("Error al enviar datos", e);
        }
    });
}
