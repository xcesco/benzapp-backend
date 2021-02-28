jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICittadino, Cittadino } from '../cittadino.model';
import { CittadinoService } from '../service/cittadino.service';

import { CittadinoRoutingResolveService } from './cittadino-routing-resolve.service';

describe('Service Tests', () => {
  describe('Cittadino routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CittadinoRoutingResolveService;
    let service: CittadinoService;
    let resultCittadino: ICittadino | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CittadinoRoutingResolveService);
      service = TestBed.inject(CittadinoService);
      resultCittadino = undefined;
    });

    describe('resolve', () => {
      it('should return existing ICittadino for existing id', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: new Cittadino(id) })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCittadino = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCittadino).toEqual(new Cittadino(123));
      });

      it('should return new ICittadino if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCittadino = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCittadino).toEqual(new Cittadino());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCittadino = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCittadino).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
