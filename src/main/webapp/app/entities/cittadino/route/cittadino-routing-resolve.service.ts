import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICittadino, Cittadino } from '../cittadino.model';
import { CittadinoService } from '../service/cittadino.service';

@Injectable({ providedIn: 'root' })
export class CittadinoRoutingResolveService implements Resolve<ICittadino> {
  constructor(private service: CittadinoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICittadino> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cittadino: HttpResponse<Cittadino>) => {
          if (cittadino.body) {
            return of(cittadino.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cittadino());
  }
}
