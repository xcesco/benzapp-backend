import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FasciaComponent } from '../list/fascia.component';
import { FasciaDetailComponent } from '../detail/fascia-detail.component';
import { FasciaUpdateComponent } from '../update/fascia-update.component';
import { FasciaRoutingResolveService } from './fascia-routing-resolve.service';

const fasciaRoute: Routes = [
  {
    path: '',
    component: FasciaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FasciaDetailComponent,
    resolve: {
      fascia: FasciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FasciaUpdateComponent,
    resolve: {
      fascia: FasciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FasciaUpdateComponent,
    resolve: {
      fascia: FasciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fasciaRoute)],
  exports: [RouterModule],
})
export class FasciaRoutingModule {}
