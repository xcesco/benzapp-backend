import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TipoCarburante } from 'app/entities/enumerations/tipo-carburante.model';
import { IRifornimento, Rifornimento } from '../rifornimento.model';

import { RifornimentoService } from './rifornimento.service';

describe('Service Tests', () => {
  describe('Rifornimento Service', () => {
    let service: RifornimentoService;
    let httpMock: HttpTestingController;
    let elemDefault: IRifornimento;
    let expectedResult: IRifornimento | IRifornimento[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RifornimentoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = new Rifornimento(0, currentDate, 0, 0, 0, 0, TipoCarburante.BENZINA);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            data: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Rifornimento', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            data: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.create(new Rifornimento()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Rifornimento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            data: currentDate.format(DATE_TIME_FORMAT),
            progressivo: 1,
            litriErogati: 1,
            sconto: 1,
            prezzoAlLitro: 1,
            tipoCarburante: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Rifornimento', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            data: currentDate.format(DATE_TIME_FORMAT),
            progressivo: 1,
            litriErogati: 1,
            sconto: 1,
            prezzoAlLitro: 1,
            tipoCarburante: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Rifornimento', () => {
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
