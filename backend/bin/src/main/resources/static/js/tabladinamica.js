document.addEventListener("DOMContentLoaded", function() {
    var tabla = document.getElementById("miTablaEstudiantes");
    var opcionesSeleccionadas = {};

    tabla.querySelectorAll('.header-with-arrow').forEach(function(th) {
        th.querySelector('.arrow').addEventListener('click', function() {
            var columna = th.querySelector('span').innerText;
            var opciones = Array.from(new Set(Array.from(tabla.querySelectorAll('tbody td:nth-child(' + (th.cellIndex + 1) + ')')).map(function(td) { return td.innerText; })));
            var select = th.querySelector('.filter-options');

            // Generamos un ID único para el selector de filtros
            var selectID = columna.replace(/\s+/g, '') + 'Filter';

            select.innerHTML = '<option value=""></option>';

            opciones.forEach(function(opcion) {
                var option = document.createElement('option');
                option.value = opcion;
                option.innerText = opcion;
                select.appendChild(option);
            });

            select.style.display = 'block';

            select.id = selectID; // Asignamos el ID único

            select.addEventListener('change', function() {
                opcionesSeleccionadas[columna] = this.value;
                filtrarTabla();
            });
        });
    });

    function filtrarTabla() {
        var filas = tabla.querySelectorAll('tbody tr');

        filas.forEach(function(fila) {
            fila.style.display = 'none';
            var mostrar = true;

            Object.keys(opcionesSeleccionadas).forEach(function(columna) {
                var valor = fila.querySelector('td:nth-child(' + (Array.from(tabla.querySelectorAll('thead th')).findIndex(function(th) { return th.querySelector('span').textContent.trim() === columna; }) + 1) + ')');

                if (opcionesSeleccionadas[columna] && valor && valor.innerText !== opcionesSeleccionadas[columna]) {
                    mostrar = false;
                }
            });

            if (mostrar) {
                fila.style.display = '';
            }
        });
    }

    var filtroID = document.getElementById('filtroID');
    filtroID.addEventListener('input', function() {
        opcionesSeleccionadas['ID'] = this.value;
        filtrarTabla();
    });
});
