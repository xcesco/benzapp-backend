import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FasciaComponent } from './list/fascia.component';
import { FasciaDetailComponent } from './detail/fascia-detail.component';
import { FasciaUpdateComponent } from './update/fascia-update.component';
import { FasciaDeleteDialogComponent } from './delete/fascia-delete-dialog.component';
import { FasciaRoutingModule } from './route/fascia-routing.module';

@NgModule({
  imports: [SharedModule, FasciaRoutingModule],
  declarations: [FasciaComponent, FasciaDetailComponent, FasciaUpdateComponent, FasciaDeleteDialogComponent],
  entryComponents: [FasciaDeleteDialogComponent],
})
export class FasciaModule {}
