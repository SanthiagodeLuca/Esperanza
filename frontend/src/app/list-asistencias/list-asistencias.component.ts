import { Component, OnInit } from '@angular/core';
import { Asistencia } from '../modelos/asistencia';
import { HttpClient } from '@angular/common/http';
import { AsistenciaService } from '../services/asistencia.service';

@Component({
  selector: 'app-list-asistencias',
  templateUrl: './list-asistencias.component.html',
  styleUrls: ['./list-asistencias.component.scss']
})
export class ListAsistenciasComponent implements OnInit {
  
  asistencias:Asistencia[]=[];
  
 constructor(private asistenciaService:AsistenciaService){}

  ngOnInit(): void {
   
    this.asistenciaService.getAsistencias().subscribe(data=>{this.asistencias = data;});
  }

}
