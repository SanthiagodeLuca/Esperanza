import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FiltroService {


  aplicarFiltros2(datos: any[], filtros: { [key: string]: any }): any[] {
    // Verifica si hay filtros y si no hay filtros aplicados
    const noHayFiltros = !filtros || Object.keys(filtros).length === 0;
  
    // Si no hay filtros aplicados, devuelve los datos sin filtrar
    if (noHayFiltros) {
      return datos;
    }
  
    // Aplica los filtros
    return datos.filter(item => {
      // Itera por cada propiedad en los filtros
      for (const key in filtros) {
        // Verifica si el filtro actual tiene un valor y si el elemento tiene esa propiedad
        if (filtros[key] && item[key]) {
          // Verifica si el valor del elemento coincide parcialmente con el valor del filtro
          if (item[key].toString().toLowerCase().includes(filtros[key].toString().toLowerCase())) {
            // Si hay coincidencia, continúa con el siguiente elemento
            continue;
          } else {
            // Si no hay coincidencia, excluye el elemento del array filtrado
            return false;
          }
        }
      }
      // Si todos los filtros coinciden para al menos una propiedad, incluye el elemento en el nuevo array filtrado
      return true;
    });
  }
  
  

  //filtros agrega categorias y es un diccionario la clave es string
  //usa de valor any que es un tipo para cualquier tipo de variable y internamente no verifica lo que hay en la variable
  //le dan un array y ciertas categorias y devuelve un array cumpliendo esas categorias
  aplicarFiltros(datos: any[], filtros: { [key: string]: any }): any[] {


// Verifica si hay filtros y si no hay filtros aplicados
const noHayFiltros = !filtros || Object.keys(filtros).length === 0;

// Si no hay filtros aplicados, devuelve los datos sin filtrar
if (noHayFiltros) {
  return datos;
}

    //verifica si no hay filtros o si no hay llaves devuelve el mismo array 
    //filtros esta null, {}!= null null=no tiene valor {}(vacio)=existe pero no tiene una propiedad definida
    // Object.keys(filtros).length === 0 o el objeto filtros esta vacio (si esta null aparecera un error)
    //es posible que filtros sea null y tenga llaves
    if (!filtros || Object.keys(filtros).length === 0) {
      return datos;
    }

    //crea un arreglo con los elementos qeue pasan una prueba
      //esto itera por cada elemento
      //var count=0;
      return datos.filter(item => {
        //itera por cada propiedad de un objeto
        
        for (const key in filtros) {
         // count=count+1;
          //console.log(count);
          console.log(`Clave: ${key}, Valor: ${filtros[key]}`);
          if (
            filtros[key] &&                  // Verifica si el valor de la propiedad en el objeto filtros existe
            item[key] &&                     // Verifica si el elemento en el array datos tiene esa propiedad
            item[key].toString().toLowerCase().includes(filtros[key].toString().toLowerCase())  // Verifica si el valor de la propiedad en el elemento del array coincide parcialmente con el valor del filtro
          ) {

            // Si todas las condiciones se cumplen para al menos una propiedad, el elemento se incluye en el nuevo array filtrado
            // Aquí se puede agregar lógica adicional si se desea
            continue; // Continúa con el siguiente elemento del array datos
          } else {
            return false; // Si alguna de las condiciones no se cumple, se excluye el elemento del nuevo array filtrado
          }
        }
        return true;
      });
    }
  }
 

