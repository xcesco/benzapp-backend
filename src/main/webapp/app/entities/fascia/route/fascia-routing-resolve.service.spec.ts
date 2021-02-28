jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFascia, Fascia } from '../fascia.model';
import { FasciaService } from '../service/fascia.service';

import { FasciaRoutingResolveService } from './fascia-routing-resolve.service';

describe('Service Tests', () => {
  describe('Fascia routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FasciaRoutingResolveService;
    let service: FasciaService;
    let resultFascia: IFascia | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FasciaRoutingResolveService);
      service = TestBed.inject(FasciaService);
      resultFascia = undefined;
    });

    describe('resolve', () => {
      it('should return existing IFascia for existing id', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: new Fascia(id) })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFascia = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFascia).toEqual(new Fascia(123));
      });

      it('should return new IFascia if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFascia = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFascia).toEqual(new Fascia());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFascia = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFascia).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
