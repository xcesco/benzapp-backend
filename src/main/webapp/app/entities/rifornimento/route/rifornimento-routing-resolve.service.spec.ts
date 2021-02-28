jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRifornimento, Rifornimento } from '../rifornimento.model';
import { RifornimentoService } from '../service/rifornimento.service';

import { RifornimentoRoutingResolveService } from './rifornimento-routing-resolve.service';

describe('Service Tests', () => {
  describe('Rifornimento routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: RifornimentoRoutingResolveService;
    let service: RifornimentoService;
    let resultRifornimento: IRifornimento | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(RifornimentoRoutingResolveService);
      service = TestBed.inject(RifornimentoService);
      resultRifornimento = undefined;
    });

    describe('resolve', () => {
      it('should return existing IRifornimento for existing id', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: new Rifornimento(id) })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRifornimento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRifornimento).toEqual(new Rifornimento(123));
      });

      it('should return new IRifornimento if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRifornimento = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultRifornimento).toEqual(new Rifornimento());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultRifornimento = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultRifornimento).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
