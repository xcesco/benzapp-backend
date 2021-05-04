import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotificaComponent } from '../list/notifica.component';
import { NotificaDetailComponent } from '../detail/notifica-detail.component';
import { NotificaUpdateComponent } from '../update/notifica-update.component';
import { NotificaRoutingResolveService } from './notifica-routing-resolve.service';

const notificaRoute: Routes = [
  {
    path: '',
    component: NotificaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificaDetailComponent,
    resolve: {
      notifica: NotificaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotificaUpdateComponent,
    resolve: {
      notifica: NotificaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotificaUpdateComponent,
    resolve: {
      notifica: NotificaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(notificaRoute)],
  exports: [RouterModule],
})
export class NotificaRoutingModule {}
