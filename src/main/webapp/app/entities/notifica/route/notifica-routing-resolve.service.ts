import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotifica, Notifica } from '../notifica.model';
import { NotificaService } from '../service/notifica.service';

@Injectable({ providedIn: 'root' })
export class NotificaRoutingResolveService implements Resolve<INotifica> {
  constructor(protected service: NotificaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotifica> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((notifica: HttpResponse<Notifica>) => {
          if (notifica.body) {
            return of(notifica.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Notifica());
  }
}
