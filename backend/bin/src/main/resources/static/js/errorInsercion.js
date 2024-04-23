/*
function mostrarError(mensaje) {
    var errorDiv = document.getElementById("errorDiv");
    errorDiv.innerText = mensaje;
    errorDiv.style.display = "block";
}

var contenidoQR = {
  // Aquí deberías definir el contenido del QR
  estudiante: "valor",
  // ...otros datos del Q
};
*/
// Realizar una solicitud GET a la URL '/listar'
/*
fetch('/listar', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(contenidoQR),
})
.then(response => {
	  // Verificar si la respuesta es exitosa (código de estado 200-299)

    if (!response.ok) {
        throw new Error('Error en la solicitud');
    }
      // Convertir la respuesta a formato JSON

    return response.json();
})
.then(data => {
    // Aquí manejas la respuesta exitosa
    console.log('Respuesta exitosa:', data);
})
.catch(error => {
    // Aquí manejas los errores
    console.error('Error:', error);

    if (error.errorAlert) {
        var errorDiv = document.getElementById("errorDiv");
        errorDiv.innerText = error.errorAlert;
        errorDiv.style.display = "block";
    } else {
        mostrarError('No se puede tomar otraves el almuerzo.');
    }
});
*/
