import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertaAsistenciaEliminarComponent } from './alerta-asistencia-eliminar.component';

describe('AlertaAsistenciaEliminarComponent', () => {
  let component: AlertaAsistenciaEliminarComponent;
  let fixture: ComponentFixture<AlertaAsistenciaEliminarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertaAsistenciaEliminarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AlertaAsistenciaEliminarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
