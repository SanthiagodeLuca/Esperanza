import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertaUserEliminarComponent } from './alerta-user-eliminar.component';

describe('AlertaUserEliminarComponent', () => {
  let component: AlertaUserEliminarComponent;
  let fixture: ComponentFixture<AlertaUserEliminarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AlertaUserEliminarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AlertaUserEliminarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
