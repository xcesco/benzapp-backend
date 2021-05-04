jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INotifica, Notifica } from '../notifica.model';
import { NotificaService } from '../service/notifica.service';

import { NotificaRoutingResolveService } from './notifica-routing-resolve.service';

describe('Service Tests', () => {
  describe('Notifica routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NotificaRoutingResolveService;
    let service: NotificaService;
    let resultNotifica: INotifica | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NotificaRoutingResolveService);
      service = TestBed.inject(NotificaService);
      resultNotifica = undefined;
    });

    describe('resolve', () => {
      it('should return INotifica returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotifica = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNotifica).toEqual({ id: 123 });
      });

      it('should return new INotifica if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotifica = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNotifica).toEqual(new Notifica());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNotifica = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNotifica).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
