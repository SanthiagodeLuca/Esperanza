import { Component, OnInit } from '@angular/core';
import { LoginService } from '../services/auth/login.service';
import { User } from '../modelos/user';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  user?:User;
  public userLoginOn:boolean=false;
  
  constructor(private loginService:LoginService){

  }
  ngOnInit():void{

    this.loginService.currentUserLoginOn.subscribe({

      next:(userLoginOn)=>{
        this.userLoginOn=userLoginOn; 
      }
    });


  }
}
