import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MarchioComponent } from './list/marchio.component';
import { MarchioDetailComponent } from './detail/marchio-detail.component';
import { MarchioUpdateComponent } from './update/marchio-update.component';
import { MarchioDeleteDialogComponent } from './delete/marchio-delete-dialog.component';
import { MarchioRoutingModule } from './route/marchio-routing.module';

@NgModule({
  imports: [SharedModule, MarchioRoutingModule],
  declarations: [MarchioComponent, MarchioDetailComponent, MarchioUpdateComponent, MarchioDeleteDialogComponent],
  entryComponents: [MarchioDeleteDialogComponent],
})
export class MarchioModule {}
