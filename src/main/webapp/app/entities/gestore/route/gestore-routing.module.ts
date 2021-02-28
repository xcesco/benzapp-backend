import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GestoreComponent } from '../list/gestore.component';
import { GestoreDetailComponent } from '../detail/gestore-detail.component';
import { GestoreUpdateComponent } from '../update/gestore-update.component';
import { GestoreRoutingResolveService } from './gestore-routing-resolve.service';

const gestoreRoute: Routes = [
  {
    path: '',
    component: GestoreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GestoreDetailComponent,
    resolve: {
      gestore: GestoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GestoreUpdateComponent,
    resolve: {
      gestore: GestoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GestoreUpdateComponent,
    resolve: {
      gestore: GestoreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gestoreRoute)],
  exports: [RouterModule],
})
export class GestoreRoutingModule {}
