jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGestore, Gestore } from '../gestore.model';
import { GestoreService } from '../service/gestore.service';

import { GestoreRoutingResolveService } from './gestore-routing-resolve.service';

describe('Service Tests', () => {
  describe('Gestore routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GestoreRoutingResolveService;
    let service: GestoreService;
    let resultGestore: IGestore | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GestoreRoutingResolveService);
      service = TestBed.inject(GestoreService);
      resultGestore = undefined;
    });

    describe('resolve', () => {
      it('should return existing IGestore for existing id', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: new Gestore(id) })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGestore = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGestore).toEqual(new Gestore(123));
      });

      it('should return new IGestore if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGestore = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGestore).toEqual(new Gestore());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGestore = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGestore).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
