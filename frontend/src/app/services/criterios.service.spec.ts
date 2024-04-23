import { TestBed } from '@angular/core/testing';

import { CriteriosService } from './criterios.service';

describe('CriteriosService', () => {
  let service: CriteriosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CriteriosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
