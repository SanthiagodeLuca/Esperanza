import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { SidebarService } from 'src/app/services/sidebar/sidebar.service';
import { SidebarComponent } from '../sidenav/sidenav.component';
import { LoginService } from 'src/app/services/auth/login.service';
import { NotificacionService } from 'src/app/services/notificacion/notificacion.service';
import  {NotificacionComponent} from 'src/app/pages/notificacion/notificacion.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  notificationCount: number = 0; // Definir la propiedad notificationCount

  userLoginOn:boolean=false;
  hasNewNotifications = false;
  showNotifications: boolean = false;
  constructor(private sidebarService: SidebarService,private loginService:LoginService,private notificationService: NotificacionService) {}

  ngOnInit():void{
    this.notificationService.notifications$.subscribe(() => {
      this.hasNewNotifications = true;
    });
    this.loginService.currentUserLoginOn.subscribe({

      next:(userLoginOn)=>{
        this.userLoginOn=userLoginOn; 
      }
    })

    
  }

  clearNotifications() {
    this.hasNewNotifications = false;
  }
  toggleSidebar() {
  // Check if the button click event is registered
    this.sidebarService.toggleSidebar();
    // Check if the visibility state is changing
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
  }
}
