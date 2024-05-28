import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RefrigerioComponent } from './refrigerio.component';

describe('RefrigerioComponent', () => {
  let component: RefrigerioComponent;
  let fixture: ComponentFixture<RefrigerioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RefrigerioComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RefrigerioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
