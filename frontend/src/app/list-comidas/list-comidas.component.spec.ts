import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListComidasComponent } from './list-comidas.component';

describe('ListComidasComponent', () => {
  let component: ListComidasComponent;
  let fixture: ComponentFixture<ListComidasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListComidasComponent]
    });
    fixture = TestBed.createComponent(ListComidasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
