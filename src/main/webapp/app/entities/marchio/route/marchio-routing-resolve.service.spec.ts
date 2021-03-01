jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMarchio, Marchio } from '../marchio.model';
import { MarchioService } from '../service/marchio.service';

import { MarchioRoutingResolveService } from './marchio-routing-resolve.service';

describe('Service Tests', () => {
  describe('Marchio routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MarchioRoutingResolveService;
    let service: MarchioService;
    let resultMarchio: IMarchio | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MarchioRoutingResolveService);
      service = TestBed.inject(MarchioService);
      resultMarchio = undefined;
    });

    describe('resolve', () => {
      it('should return existing IMarchio for existing id', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: new Marchio(id) })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMarchio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMarchio).toEqual(new Marchio(123));
      });

      it('should return new IMarchio if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMarchio = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMarchio).toEqual(new Marchio());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMarchio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMarchio).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
