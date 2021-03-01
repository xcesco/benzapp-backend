import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MarchioComponent } from '../list/marchio.component';
import { MarchioDetailComponent } from '../detail/marchio-detail.component';
import { MarchioUpdateComponent } from '../update/marchio-update.component';
import { MarchioRoutingResolveService } from './marchio-routing-resolve.service';

const marchioRoute: Routes = [
  {
    path: '',
    component: MarchioComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MarchioDetailComponent,
    resolve: {
      marchio: MarchioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MarchioUpdateComponent,
    resolve: {
      marchio: MarchioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MarchioUpdateComponent,
    resolve: {
      marchio: MarchioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(marchioRoute)],
  exports: [RouterModule],
})
export class MarchioRoutingModule {}
