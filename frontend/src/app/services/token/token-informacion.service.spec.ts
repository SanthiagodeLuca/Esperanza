import { TestBed } from '@angular/core/testing';

import { TokenInformacionService } from './token-informacion.service';

describe('TokenInformacionService', () => {
  let service: TokenInformacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenInformacionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
