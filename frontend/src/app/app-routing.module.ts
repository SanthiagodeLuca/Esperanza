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

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'timetable', component: TimetableComponent },
  {path:'estudiantes',component:EstudiantesComponent},
  {path:'registro',component:RegistroComponent},
  {path:'excel',component:ExcelComponent},
  {path:'seguridad',component:SeguridadComponent},
  {path:'modificar',component:ModificarComponent},
  {path:'registro/diario',component:DiarioComponent},
  {path:'registro/semanal',component:SemanalComponent},
  {path:'registro/mensual',component:MensualComponent},
  {path:'comidas',component:ComidasComponent},
  {path:'criterio',component:CriteriosComponent}

];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
