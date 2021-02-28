import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { TipoVeicolo } from 'app/entities/enumerations/tipo-veicolo.model';
import { TipoCarburante } from 'app/entities/enumerations/tipo-carburante.model';
import { ITessera, Tessera } from '../tessera.model';

import { TesseraService } from './tessera.service';

describe('Service Tests', () => {
  describe('Tessera Service', () => {
    let service: TesseraService;
    let httpMock: HttpTestingController;
    let elemDefault: ITessera;
    let expectedResult: ITessera | ITessera[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TesseraService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = new Tessera(
        0,
        'AAAAAAA',
        currentDate,
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        TipoVeicolo.CICLOMOTORE,
        TipoCarburante.BENZINA
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataEmissione: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Tessera', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataEmissione: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataEmissione: currentDate,
          },
          returnedFromService
        );

        service.create(new Tessera()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tessera', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            codice: 'BBBBBB',
            dataEmissione: currentDate.format(DATE_TIME_FORMAT),
            immagine: 'BBBBBB',
            targa: 'BBBBBB',
            veicolo: 'BBBBBB',
            carburante: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataEmissione: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tessera', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            codice: 'BBBBBB',
            dataEmissione: currentDate.format(DATE_TIME_FORMAT),
            immagine: 'BBBBBB',
            targa: 'BBBBBB',
            veicolo: 'BBBBBB',
            carburante: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataEmissione: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Tessera', () => {
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
