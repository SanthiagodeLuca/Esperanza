
//esta pendiente el evento de que "DOMContentLoaded" ocurra en el objeto document 
// "DOMContentLoaded" se dispara cuando el HTML de la página ha sido completamente cargado y parseado, sin esperar a que se carguen las imágenes y otros recursos externos. 
document.addEventListener("DOMContentLoaded", function() {
	    //console.log("DOMContentLoaded event triggered");
    var tabla = document.getElementById("tablaAsistencias");
  
   var opcionesSeleccionadas = {};

    tabla.querySelectorAll('.header-with-arrow').forEach(function(th) {
				console.log("funciona")
		        th.querySelector('.arrow').addEventListener('click', function() {
				           //obtiene el texto dentro de ese elemento
				            var columna = th.querySelector('span').innerText;
				            console.log("columna",columna);
				           //tabla.querySelectorAll('tbody td:nth-child(' + (th.cellIndex + 1) + ')') Esto selecciona todos los elementos td que son hijos de tbody
				           //querySelectorAll. Esto es necesario porque querySelectorAll devuelve una NodeList
				           //.map(function(td) { return td.innerText; }): Este mapeo toma cada elemento td en el array resultante y devuelve el texto contenido dentro de él (td.innerText)
				           //new Set(...): Crea un nuevo objeto Set a partir del array resultante del mapeo. Un objeto Set es una colección de valores únicos, lo que significa que no puede haber duplicados.
				           
				            var opciones = Array.from(new Set(Array.from(tabla.querySelectorAll('tbody td:nth-child(' + (th.cellIndex + 1) + ')')).map(function(td) { return td.innerText; })));
				          console.log("opciones",opciones);
				            var select = th.querySelector('.filter-options');
				
				            select.innerHTML = '<option value=""></option>';
				            
				            //opciones.forEach(...): Aquí estamos utilizando la función forEach en el array opciones.

								//(function(opcion) {}):Esto es una función anónima que toma un argumento opcion. Esta función se ejecutará para cada elemento del array.
								
								//opcion: Es el elemento actual del array en cada iteración.
								          //aqui mete todas la opciones al selector
								            opciones.forEach(function(opcion) {
												console.log("se ejecuta opciones")
								
								                var option = document.createElement('option'); // Crea un elemento <option>
											    option.value = opcion; // Establece el valor de la opción
											    option.innerText = opcion; // Establece el texto visible de la opción
											    
											    select.appendChild(option); // Añade la opción al elemento 'select'
           				 });
           				 
           				 //cambia la etiqueta select para que sea visible
			            select.style.display = 'block';
            			console.log("se ejecuta el select")
            			//adiciona el evento change que cuando se hace click en un elemento llama a la funcion
        			    select.addEventListener('change', function() {
						//this value coge el valor de la opcion seleccionada
			                opcionesSeleccionadas[columna] = this.value;
			                console.log("valor de la opcion seleccionada",this.value)
			                console.log("opciones seleccionadas",opcionesSeleccionadas[columna])
			               // console.log("opciones seleccionadas",opcionesSeleccionadas)
			                
		                	filtrarTabla();
          				});

				            
					});
				});
				
					 function filtrarTabla() {
						 //Esto selecciona todas las filas (tr) dentro del cuerpo (tbody) 
				        var filas = tabla.querySelectorAll('tbody tr');
				
				        filas.forEach(function(fila) {
							//console.log("se ejecuta la filas")
							//fila no visible
							console.log("valor de la fila",fila)
				            fila.style.display = 'none';
				            var mostrar = false;
							//Itera sobre las claves (columnas) del objeto 
							//object keys metodo que devulve un array de las claves del objeto
				            Object.keys(opcionesSeleccionadas).forEach(function(columna) {
							//encontrar el td especifico de una fila
				               // var valor = fila.querySelector('td:nth-child(' + (Array.from(tabla.querySelectorAll('thead th')).findIndex(function(th) { return th.innerText === columna; }) + 1) + ')');
								
								var valor=fila.querySelector('td:nth-child(' + (Array.from(tabla.querySelectorAll('thead th')).findIndex(function(th){ console.log("th",th); return th.querySelector('span').textContent.trim()===columna})+ 1) + ')');
							//	console.log("todo lo que llevamos",(Array.from(tabla.querySelectorAll('thead th')).findIndex(function(th){ console.log("th",th); return th.querySelector('span').textContent.trim()===columna})));
							//	console.log("valor", valor);

				            //   console.log("columna", columna);

				               //console.log("valor",valor.innerText)
				               
				                if (opcionesSeleccionadas[columna] && valor && valor.innerText === opcionesSeleccionadas[columna]) {
				                    mostrar = true;
				            console.log("entro")
				                }
				            });
				
				            if (mostrar) {
				                fila.style.display = '';
				            }
				        });
	   				 }
				
			});

/*



//adiciona el evento change que cuando se hace click en un elemento llama a la funcion
            select.addEventListener('change', function() {
				//this value coge el valor de la opcion seleccionada
                opcionesSeleccionadas[columna] = this.value;
                filtrarTabla();
            });
        });
    });

    function filtrarTabla() {
        var filas = tabla.querySelectorAll('tbody tr');

        filas.forEach(function(fila) {
			console.log("se ejecuta la fila")
            fila.style.display = 'none';
            var mostrar = true;

            Object.keys(opcionesSeleccionadas).forEach(function(columna) {
                var valor = fila.querySelector('td:nth-child(' + (Array.from(tabla.querySelectorAll('thead th')).findIndex(function(th) { return th.innerText === columna; }) + 1) + ')');
                if (opcionesSeleccionadas[columna] && valor && valor.innerText !== opcionesSeleccionadas[columna]) {
                    mostrar = false;
                }
            });

            if (mostrar) {
                fila.style.display = '';
            }
        });
    }

    // Agrega el evento de escucha para el campo de filtro de ID
    var filtroID = document.getElementById('filtroID');
    filtroID.addEventListener('input', function() {
        opcionesSeleccionadas['ID'] = this.value;
        filtrarTabla();
    });
    
    */

