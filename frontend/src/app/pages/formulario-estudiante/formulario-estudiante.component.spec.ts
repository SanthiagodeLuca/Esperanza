import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioEstudianteComponent } from './formulario-estudiante.component';

describe('FormularioEstudianteComponent', () => {
  let component: FormularioEstudianteComponent;
  let fixture: ComponentFixture<FormularioEstudianteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioEstudianteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioEstudianteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
