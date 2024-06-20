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
import { HorarioComponent } from './pages/horario/horario.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatNativeDateModule, MatOptionModule } from '@angular/material/core'; // MatOption is part of MatCoreModule

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { DesayunoComponent } from './pages/horario/desayuno/desayuno.component';
import { AlmuerzoComponent } from './pages/horario/almuerzo/almuerzo.component';
import { RefrigerioComponent } from './pages/horario/refrigerio/refrigerio.component';
import { NotificacionComponent } from './pages/notificacion/notificacion.component';
import { AlertaComponent } from './pages/alerta/alerta.component';
import { EstadisticasComponent } from './pages/estadisticas/estadisticas.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { ChartModule } from 'angular-highcharts';
import { PastelComponent } from './graficas/pastel/pastel.component';
import { BarrasComponent } from './graficas/barras/barras.component';
import { LineaComponent } from './graficas/linea/linea.component';
import { EditarComponent } from './pages/editar/editar.component';
import { FormularioEstudianteComponent } from './pages/formulario-estudiante/formulario-estudiante.component';
import { FormularioCrearEstudianteComponent } from './pages/formulario-crear-estudiante/formulario-crear-estudiante.component';
import { AlertaEstudianteEliminarComponent } from './pages/alerta/alerta-estudiante-eliminar/alerta-estudiante-eliminar.component';
import { FormularioUsuarioComponent } from './pages/formulario-usuario/formulario-usuario.component';
import { ListUserComponent } from './list-user/list-user.component';
import { FormularioCrearUsuarioComponent } from './pages/formulario-crear-usuario/formulario-crear-usuario.component';


//import { MatMomentDateModule } from '@angular/material/datemodule';


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
    LoginComponent,
    HorarioComponent,
    DesayunoComponent,
    AlmuerzoComponent,
    RefrigerioComponent,
    NotificacionComponent,
    AlertaComponent,
    EstadisticasComponent,
    PastelComponent,
    BarrasComponent,
    LineaComponent,
    EditarComponent,
    FormularioEstudianteComponent,
    FormularioCrearEstudianteComponent,
    AlertaEstudianteEliminarComponent,
    ListUserComponent,
    FormularioUsuarioComponent,
    FormularioCrearUsuarioComponent
  
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

    ReactiveFormsModule,

    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule, // Add this module


    MatDatepickerModule,        // <----- import(must)
    MatNativeDateModule,
    MatInputModule ,
    
    ChartModule,
    
    
    // <----- import for date formating(optional)
   // MatMomentDateModule
   
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorService, multi: true },
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
