import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TesseraComponent } from '../list/tessera.component';
import { TesseraDetailComponent } from '../detail/tessera-detail.component';
import { TesseraUpdateComponent } from '../update/tessera-update.component';
import { TesseraRoutingResolveService } from './tessera-routing-resolve.service';

const tesseraRoute: Routes = [
  {
    path: '',
    component: TesseraComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TesseraDetailComponent,
    resolve: {
      tessera: TesseraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TesseraUpdateComponent,
    resolve: {
      tessera: TesseraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TesseraUpdateComponent,
    resolve: {
      tessera: TesseraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tesseraRoute)],
  exports: [RouterModule],
})
export class TesseraRoutingModule {}
