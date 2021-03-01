import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMarchio, Marchio } from '../marchio.model';
import { MarchioService } from '../service/marchio.service';

@Injectable({ providedIn: 'root' })
export class MarchioRoutingResolveService implements Resolve<IMarchio> {
  constructor(private service: MarchioService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMarchio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((marchio: HttpResponse<Marchio>) => {
          if (marchio.body) {
            return of(marchio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Marchio());
  }
}
