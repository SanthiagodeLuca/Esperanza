import { Component, OnInit } from '@angular/core';
import { Comidas } from '../modelos/comidas';
import { ComidaService } from '../services/comida.service';

@Component({
  selector: 'app-list-comidas',
  templateUrl: './list-comidas.component.html',
  styleUrls: ['./list-comidas.component.scss']
})
export class ListComidasComponent implements OnInit{
  comidas:Comidas[]=[];

  constructor(private comidasServices:ComidaService){}

  ngOnInit():void{


    this.comidasServices.getComidas().subscribe(data=>{this.comidas=data});
  }


}
