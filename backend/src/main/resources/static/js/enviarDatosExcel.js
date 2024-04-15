// JavaScript en tu página HTML
function obtenerDatosTabla() {
    var datos = [];
    var filas = document.querySelectorAll('table tbody tr');

    filas.forEach(function(fila) {
        var celdas = fila.querySelectorAll('td');
        var filaDatos = [];

        celdas.forEach(function(celda) {
		//	console.log("el contenido de la celda",celda.textContent);
            filaDatos.push(celda.textContent);
        });

        datos.push(filaDatos);
    });

    return datos;
}
// JavaScript en tu página HTML
function enviarDatosAlServidor(datos) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/actualizarDatosInforme', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    
    xhr.onload = function() {
        if (xhr.status === 200) {
            alert('Datos actualizados correctamente.');
        } else {
            alert('Error al actualizar los datos.');
        }
    };

    xhr.send(JSON.stringify(datos));
}

function enviarDatosTablaAlServidor() {
    var datos = obtenerDatosTabla();
      console.log('Datos:', datos);  // Llamas a tu función para obtener los datos de la tabla
    enviarDatosAlServidor(datos);     // Llamas a la función para enviar los datos al servidor
}
