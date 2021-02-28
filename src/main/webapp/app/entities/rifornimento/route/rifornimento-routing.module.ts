import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RifornimentoComponent } from '../list/rifornimento.component';
import { RifornimentoDetailComponent } from '../detail/rifornimento-detail.component';
import { RifornimentoUpdateComponent } from '../update/rifornimento-update.component';
import { RifornimentoRoutingResolveService } from './rifornimento-routing-resolve.service';

const rifornimentoRoute: Routes = [
  {
    path: '',
    component: RifornimentoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RifornimentoDetailComponent,
    resolve: {
      rifornimento: RifornimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RifornimentoUpdateComponent,
    resolve: {
      rifornimento: RifornimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RifornimentoUpdateComponent,
    resolve: {
      rifornimento: RifornimentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rifornimentoRoute)],
  exports: [RouterModule],
})
export class RifornimentoRoutingModule {}
