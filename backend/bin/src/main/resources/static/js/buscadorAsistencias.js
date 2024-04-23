
console.log("El archivo buscador Asistencia se está ejecutando correctamente.");
//console.log(miTablaAsistencias);
document.getElementById("buscadorAsistencias").addEventListener("input", function()	 {
  //this.value.toLowerCase obtiene el valor del campo de busqueda
    var textoBusqueda = this.value.toLowerCase();
    //obtiene el valor del atributo de la tabla
    var tablaId = this.getAttribute('data-table');
    console.log("valor",tablaId)
    
    //obtiene la tabla utilizando el ID extraido dl atributo
    var tabla = document.getElementById(tablaId);
    
    
    //obtiene las filas de la tabla
    var filas = tabla.getElementsByTagName('tr');
    console.log("filas",filas)
//ignora la fila 1

    for (var i = 1; i < filas.length; i++) {
     //obtiene la primera celda
        var celda = filas[i].getElementsByTagName('td')[1];
 //  var celda= filas[i].getElementsByTagName('td');
   // console.log("celda",celda) 
   
        if (celda) {
          //obtiene texto dentro de la celda
            var textoCelda = celda.textContent.toLowerCase();
                console.log("valor de la celda",textoCelda) 
				console.log("texto Busqueda",textoBusqueda)
            //muestra la fila si el texto de la celda contiene el texto de busq   ueda si es asi  ''(muestra) y si no es asi 'oculta'
            filas[i].style.display = textoCelda.includes(textoBusqueda) ? '' : 'none';
          
        }
    }
});
