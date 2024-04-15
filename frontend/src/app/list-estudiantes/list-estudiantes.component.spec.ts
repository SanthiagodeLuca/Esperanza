import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListEstudiantesComponent } from './list-estudiantes.component';

describe('ListEstudiantesComponent', () => {
  let component: ListEstudiantesComponent;
  let fixture: ComponentFixture<ListEstudiantesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListEstudiantesComponent]
    });
    fixture = TestBed.createComponent(ListEstudiantesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
