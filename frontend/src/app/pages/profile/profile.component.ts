import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/modelos/user';
import { LoginService } from 'src/app/services/auth/login.service';
import { TokenInformacionService } from 'src/app/services/token/token-informacion.service';
import { UserService } from 'src/app/services/user/user.service';
import { environment } from 'src/enviroments/enviroments';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
  public user?:User;
  errorMessage:string="";
 
  constructor(private userService:UserService,private loginService:LoginService,private router:Router,private tokenInformacionService:TokenInformacionService){

    const userLoginOn=this.loginService.currentUserLoginOn;
    //console.log(userLoginOn)
    this.userService.getUser(tokenInformacionService.getUserIdFromToken()).subscribe({

      next:(userData)=>{
        this.user=userData;
      },
      error:(errorData)=>{
          this.errorMessage=errorData;
      } ,
      complete:()=>{

        console.info("user OK Data");
      }

    });
  }
  address = '123 Main St, City, Country';
  isEditMode = false;
  editAddress() {
    // Implement your address editing logic here
    // You can open a dialog or navigate to an edit page
    // For now, let's just change the address to a placeholder value
    this.address = 'New Address, City, Country';
  }


  toggleEditMode() {
    this.isEditMode = !this.isEditMode;
  }

  saveChanges() {
    // Perform save logic here (e.g., update the backend)
    // After saving, exit edit mode
    this.isEditMode = false;
  }

  cancelEdit() {
    // Reset any changes made during edit and exit edit mode
    this.isEditMode = false;
  }

  logOut() {
    this.loginService.logOut(); // Llama al método logOut() de LoginService
    this.router.navigateByUrl("/login");
  }
}
