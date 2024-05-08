import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';


import {MatButtonModule} from '@angular/material/button';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './layout/header/header.component';
import { SidebarComponent } from './layout/sidenav/sidenav.component';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MainComponent } from './main/main.component';
import { RouterLinkActiveExactDirective } from './main/appRouterLinkActiveExact.directive';
import { ProfileComponent } from './pages/profile/profile.component';
import { MatCardModule } from '@angular/material/card';
import { TimetableComponent } from './pages/timetable/timetable.component';
import { EstudiantesComponent } from './estudiantes/estudiantes.component';
import { RegistroComponent } from './registro/registro.component';
import { ExcelComponent } from './excel/excel.component';
import { SeguridadComponent } from './seguridad/seguridad.component';
import { ModificarComponent } from './modificar/modificar.component';
import { DiarioComponent } from './pages/diario/diario.component';
import { SemanalComponent } from './pages/semanal/semanal.component';
import { MensualComponent } from './pages/mensual/mensual.component';
import { ListEstudiantesComponent } from './list-estudiantes/list-estudiantes.component';
import { EstudianteService } from './services/estudiante.service';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ComidasComponent } from './comidas/comidas.component';
import { ListComidasComponent } from './list-comidas/list-comidas.component';
import { ListAsistenciasComponent } from './list-asistencias/list-asistencias.component';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import  {CommonModule} from '@angular/common';
import { CriteriosComponent } from './criterio/criterio.component';
import { LoginComponent } from './login/login.component';
//import { JwtInterceptorService } from './services/auth/jwt-interceptor.service';
import { JwtInterceptorService } from './services/auth/jwt-interceptor.service';


// Import FormsModule
@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SidebarComponent,
    HomeComponent,
    DashboardComponent,
    MainComponent,
    RouterLinkActiveExactDirective,
    ProfileComponent,
    TimetableComponent,
    EstudiantesComponent,
    RegistroComponent,
    ExcelComponent,
    SeguridadComponent,
    ModificarComponent,
    DiarioComponent,
    SemanalComponent,
    MensualComponent,
    ListEstudiantesComponent,
    ComidasComponent,
    ListComidasComponent,
    ListAsistenciasComponent,
    CriteriosComponent,
    LoginComponent
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    // * MATERIAL IMPORTS
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatIconModule,
    MatDividerModule,
    MatListModule,
    MatButtonModule,
    MatCardModule,
   
    MatTableModule,
    HttpClientModule,
    TableModule,
    InputTextModule,
    DropdownModule ,
    FormsModule,
    CommonModule,

    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorService, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
