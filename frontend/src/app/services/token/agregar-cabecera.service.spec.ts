import { TestBed } from '@angular/core/testing';

import { AgregarCabeceraService } from './agregar-cabecera.service';

describe('AgregarCabeceraService', () => {
  let service: AgregarCabeceraService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgregarCabeceraService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
