import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITessera, Tessera } from '../tessera.model';
import { TesseraService } from '../service/tessera.service';

@Injectable({ providedIn: 'root' })
export class TesseraRoutingResolveService implements Resolve<ITessera> {
  constructor(private service: TesseraService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITessera> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tessera: HttpResponse<Tessera>) => {
          if (tessera.body) {
            return of(tessera.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tessera());
  }
}
