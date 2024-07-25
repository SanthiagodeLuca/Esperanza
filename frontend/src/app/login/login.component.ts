    import { Component, OnInit } from '@angular/core';
    import {FormBuilder, FormGroup,Validator, Validators} from '@angular/forms'
    import { Router } from '@angular/router';
    import { LoginService } from '../services/auth/login.service';
    import { loginRequest } from '../modelos/loginRequest';
    import { HttpErrorResponse } from '@angular/common/http';
    @Component({
      selector: 'app-login',
      templateUrl: './login.component.html',
      styleUrl: './login.component.scss'
    })
    export class LoginComponent implements OnInit{
    
      loginError:string="";
      loginForm=this.formBuilder.group({
        username:['',[Validators.required,Validators.email]],
        password:['',[Validators.required]],
      
      
      
      })
        
      //inyectar la clase formbuilder
      constructor(private formBuilder:FormBuilder,private router:Router,private loginService:LoginService){}
      

      ngOnInit(): void{


      }
    //se  llama cuando se presione el iniciar sesion
    /* login(){
        if(this.loginForm.valid){
          //console.log("LLamar al servicio Login")
          this.loginService.login(this.loginForm.value as loginRequest).subscribe({
            next:(userData)=>{
              console.log(userData)},
              error: (errorData) => {
                errorData.subscribe({
                  next: (error: { message: string; }) => {
                    console.log(error);
                    this.loginError = error.message || 'Error desconocido';
                  }
                });
              },
              complete:()=>{

                console.log("login esta completo")
                this.router.navigateByUrl("/dashboard")
                this.loginForm.reset();
              }
            
            
            });
        
        }else{

        this.loginForm.markAllAsTouched();
        }

      }*/
      login() {
        if (this.loginForm.valid) {
          this.loginService.solicitud(this.loginForm.value as loginRequest).subscribe({
            next: (response: any) => {
              if (response.token) {
                console.log("Login exitoso. Token recibido:", response.token);
                // Redirigir al dashboard o realizar otras acciones necesarias
                this.router.navigateByUrl("/registro");
              this.loginForm.reset();
              } else if (response.error) {
                console.error("Error durante el inicio de sesión:", response.error);
                this.loginError = "Error durante el inicio de sesión. Por favor, inténtelo de nuevo.";
              } else {
                console.error("Respuesta del servidor inesperada:", response);
                this.loginError = "Respuesta del servidor inesperada. Por favor, inténtelo de nuevo más tarde.";
              }
            },
            error: (error: HttpErrorResponse) => {
              console.error("Error de HTTP:", error);
              this.loginError = "Error de conexión. Por favor, inténtelo de nuevo más tarde.";
            }
          });
        } else {
          this.loginForm.markAllAsTouched();
        }
      }
      
      //propiedad get
      get username(){
        return this.loginForm.controls.username;
      }
      get password(){

        return this.loginForm.controls.password;
      }

    }
