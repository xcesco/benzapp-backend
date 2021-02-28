import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGestore, Gestore } from '../gestore.model';
import { GestoreService } from '../service/gestore.service';

@Injectable({ providedIn: 'root' })
export class GestoreRoutingResolveService implements Resolve<IGestore> {
  constructor(private service: GestoreService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGestore> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gestore: HttpResponse<Gestore>) => {
          if (gestore.body) {
            return of(gestore.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gestore());
  }
}
