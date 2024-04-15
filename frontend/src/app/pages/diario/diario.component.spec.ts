import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiarioComponent } from './diario.component';

describe('DiarioComponent', () => {
  let component: DiarioComponent;
  let fixture: ComponentFixture<DiarioComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DiarioComponent]
    });
    fixture = TestBed.createComponent(DiarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
