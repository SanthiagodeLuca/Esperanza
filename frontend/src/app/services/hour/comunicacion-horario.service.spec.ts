import { TestBed } from '@angular/core/testing';

import { ComunicacionHorarioService } from './comunicacion-horario.service';

describe('ComunicacionHorarioService', () => {
  let service: ComunicacionHorarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComunicacionHorarioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
