import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioCrearUsuarioComponent } from './formulario-crear-usuario.component';

describe('FormularioCrearUsuarioComponent', () => {
  let component: FormularioCrearUsuarioComponent;
  let fixture: ComponentFixture<FormularioCrearUsuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioCrearUsuarioComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioCrearUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
