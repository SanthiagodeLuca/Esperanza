import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { SidebarService } from 'src/app/services/sidebar/sidebar.service';
import { SidebarComponent } from '../sidenav/sidenav.component';
import { LoginService } from 'src/app/services/auth/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  userLoginOn:boolean=false;
  constructor(private sidebarService: SidebarService,private loginService:LoginService) {}

  ngOnInit():void{

    this.loginService.currentUserLoginOn.subscribe({

      next:(userLoginOn)=>{
        this.userLoginOn=userLoginOn; 
      }
    })
  }
  toggleSidebar() {
  // Check if the button click event is registered
    this.sidebarService.toggleSidebar();
    // Check if the visibility state is changing
  }
}
