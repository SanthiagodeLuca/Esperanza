import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { TimetableComponent } from './pages/timetable/timetable.component';
import { EstudiantesComponent } from './estudiantes/estudiantes.component';
import { RegistroComponent } from './registro/registro.component';
import { ExcelComponent } from './excel/excel.component';
import { SeguridadComponent } from './seguridad/seguridad.component';
import { ModificarComponent } from './modificar/modificar.component';
import { DiarioComponent } from './pages/diario/diario.component';
import { SemanalComponent } from './pages/semanal/semanal.component';
import { MensualComponent } from './pages/mensual/mensual.component';
import { ComidasComponent } from './comidas/comidas.component';
import { CriteriosComponent } from './criterio/criterio.component';
import { LoginComponent } from './login/login.component';
import { HorarioComponent } from './pages/horario/horario.component';
import { AlmuerzoComponent } from './pages/horario/almuerzo/almuerzo.component';
import { DesayunoComponent } from './pages/horario/desayuno/desayuno.component';
import { RefrigerioComponent } from './pages/horario/refrigerio/refrigerio.component';
import { NotificacionComponent } from './pages/notificacion/notificacion.component';
import { AlertaComponent } from './pages/alerta/alerta.component';
import { AuthGuard } from './auth.guard';
import { MainComponent } from './main/main.component';
import { EstadisticasComponent } from './pages/estadisticas/estadisticas.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent }, // Esta coma estaba mal ubicada
  {    
    path: '',
    component: MainComponent,
    canActivate: [AuthGuard], // Protege el layout principal
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'profile', component: ProfileComponent },
      { path: 'timetable', component: TimetableComponent },
      { path: 'estudiantes', component: EstudiantesComponent },
      { path: 'registro', component: RegistroComponent },
      { path: 'excel', component: ExcelComponent },
      { path: 'seguridad', component: SeguridadComponent },
      { path: 'modificar', component: ModificarComponent },
      { path: 'registro/diario', component: DiarioComponent },
      { path: 'registro/semanal', component: SemanalComponent },
      { path: 'registro/mensual', component: MensualComponent },
      { path: 'comidas', component: ComidasComponent },
      { path: 'criterio', component: CriteriosComponent },
      { path: 'horario', component: HorarioComponent },
      { path: 'almuerzo', component: AlmuerzoComponent },
      { path: 'desayuno', component: DesayunoComponent },
      { path: 'refrigerio', component: RefrigerioComponent },
      { path: 'notificacion', component: NotificacionComponent },
      { path: 'alerta', component: AlertaComponent },
      {path:'estadisticas',component:EstadisticasComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
