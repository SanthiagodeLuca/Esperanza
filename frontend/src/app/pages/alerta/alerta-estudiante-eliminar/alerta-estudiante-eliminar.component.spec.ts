import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertaEstudianteEliminarComponent } from './alerta-estudiante-eliminar.component';

describe('AlertaEstudianteEliminarComponent', () => {
  let component: AlertaEstudianteEliminarComponent;
  let fixture: ComponentFixture<AlertaEstudianteEliminarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertaEstudianteEliminarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AlertaEstudianteEliminarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
