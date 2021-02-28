import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRifornimento, Rifornimento } from '../rifornimento.model';
import { RifornimentoService } from '../service/rifornimento.service';

@Injectable({ providedIn: 'root' })
export class RifornimentoRoutingResolveService implements Resolve<IRifornimento> {
  constructor(private service: RifornimentoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRifornimento> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rifornimento: HttpResponse<Rifornimento>) => {
          if (rifornimento.body) {
            return of(rifornimento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rifornimento());
  }
}
