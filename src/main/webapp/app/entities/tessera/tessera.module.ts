import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TesseraComponent } from './list/tessera.component';
import { TesseraDetailComponent } from './detail/tessera-detail.component';
import { TesseraUpdateComponent } from './update/tessera-update.component';
import { TesseraDeleteDialogComponent } from './delete/tessera-delete-dialog.component';
import { TesseraRoutingModule } from './route/tessera-routing.module';

@NgModule({
  imports: [SharedModule, TesseraRoutingModule],
  declarations: [TesseraComponent, TesseraDetailComponent, TesseraUpdateComponent, TesseraDeleteDialogComponent],
  entryComponents: [TesseraDeleteDialogComponent],
})
export class TesseraModule {}
