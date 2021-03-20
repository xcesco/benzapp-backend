jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDelega, Delega } from '../delega.model';
import { DelegaService } from '../service/delega.service';

import { DelegaRoutingResolveService } from './delega-routing-resolve.service';

describe('Service Tests', () => {
  describe('Delega routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DelegaRoutingResolveService;
    let service: DelegaService;
    let resultDelega: IDelega | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DelegaRoutingResolveService);
      service = TestBed.inject(DelegaService);
      resultDelega = undefined;
    });

    describe('resolve', () => {
      it('should return existing IDelega for existing id', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: new Delega(id) })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDelega = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDelega).toEqual(new Delega(123));
      });

      it('should return new IDelega if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDelega = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDelega).toEqual(new Delega());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDelega = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDelega).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
