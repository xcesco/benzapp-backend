import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotifica, Notifica } from '../notifica.model';

import { NotificaService } from './notifica.service';

describe('Service Tests', () => {
  describe('Notifica Service', () => {
    let service: NotificaService;
    let httpMock: HttpTestingController;
    let elemDefault: INotifica;
    let expectedResult: INotifica | INotifica[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(NotificaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        targa: 'AAAAAAA',
        data: currentDate,
      };
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

      it('should create a Notifica', () => {
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

        service.create(new Notifica()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Notifica', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            targa: 'BBBBBB',
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Notifica', () => {
        const patchObject = Object.assign(
          {
            targa: 'BBBBBB',
          },
          new Notifica()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Notifica', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            targa: 'BBBBBB',
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Notifica', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addNotificaToCollectionIfMissing', () => {
        it('should add a Notifica to an empty array', () => {
          const notifica: INotifica = { id: 123 };
          expectedResult = service.addNotificaToCollectionIfMissing([], notifica);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(notifica);
        });

        it('should not add a Notifica to an array that contains it', () => {
          const notifica: INotifica = { id: 123 };
          const notificaCollection: INotifica[] = [
            {
              ...notifica,
            },
            { id: 456 },
          ];
          expectedResult = service.addNotificaToCollectionIfMissing(notificaCollection, notifica);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Notifica to an array that doesn't contain it", () => {
          const notifica: INotifica = { id: 123 };
          const notificaCollection: INotifica[] = [{ id: 456 }];
          expectedResult = service.addNotificaToCollectionIfMissing(notificaCollection, notifica);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(notifica);
        });

        it('should add only unique Notifica to an array', () => {
          const notificaArray: INotifica[] = [{ id: 123 }, { id: 456 }, { id: 99063 }];
          const notificaCollection: INotifica[] = [{ id: 123 }];
          expectedResult = service.addNotificaToCollectionIfMissing(notificaCollection, ...notificaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const notifica: INotifica = { id: 123 };
          const notifica2: INotifica = { id: 456 };
          expectedResult = service.addNotificaToCollectionIfMissing([], notifica, notifica2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(notifica);
          expect(expectedResult).toContain(notifica2);
        });

        it('should accept null and undefined values', () => {
          const notifica: INotifica = { id: 123 };
          expectedResult = service.addNotificaToCollectionIfMissing([], null, notifica, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(notifica);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
