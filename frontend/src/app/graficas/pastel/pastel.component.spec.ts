import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PastelComponent } from './pastel.component';

describe('PastelComponent', () => {
  let component: PastelComponent;
  let fixture: ComponentFixture<PastelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PastelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PastelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
