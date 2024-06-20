import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioCrearEstudianteComponent } from './formulario-crear-estudiante.component';

describe('FormularioCrearEstudianteComponent', () => {
  let component: FormularioCrearEstudianteComponent;
  let fixture: ComponentFixture<FormularioCrearEstudianteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioCrearEstudianteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioCrearEstudianteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
