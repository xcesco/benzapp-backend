import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFascia, Fascia } from '../fascia.model';
import { FasciaService } from '../service/fascia.service';

@Injectable({ providedIn: 'root' })
export class FasciaRoutingResolveService implements Resolve<IFascia> {
  constructor(private service: FasciaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFascia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fascia: HttpResponse<Fascia>) => {
          if (fascia.body) {
            return of(fascia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fascia());
  }
}
