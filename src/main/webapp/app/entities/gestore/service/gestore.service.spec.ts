import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TipoImpianto } from 'app/entities/enumerations/tipo-impianto.model';
import { IGestore, Gestore } from '../gestore.model';

import { GestoreService } from './gestore.service';

describe('Service Tests', () => {
  describe('Gestore Service', () => {
    let service: GestoreService;
    let httpMock: HttpTestingController;
    let elemDefault: IGestore;
    let expectedResult: IGestore | IGestore[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GestoreService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = new Gestore(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 0, TipoImpianto.AUTOSTRADALE);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Gestore', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Gestore()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Gestore', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            provincia: 'BBBBBB',
            comune: 'BBBBBB',
            indirizzo: 'BBBBBB',
            longitudine: 1,
            latitudine: 1,
            tipo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Gestore', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            provincia: 'BBBBBB',
            comune: 'BBBBBB',
            indirizzo: 'BBBBBB',
            longitudine: 1,
            latitudine: 1,
            tipo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Gestore', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
