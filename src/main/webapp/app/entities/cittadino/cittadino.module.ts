import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CittadinoComponent } from './list/cittadino.component';
import { CittadinoDetailComponent } from './detail/cittadino-detail.component';
import { CittadinoUpdateComponent } from './update/cittadino-update.component';
import { CittadinoDeleteDialogComponent } from './delete/cittadino-delete-dialog.component';
import { CittadinoRoutingModule } from './route/cittadino-routing.module';

@NgModule({
  imports: [SharedModule, CittadinoRoutingModule],
  declarations: [CittadinoComponent, CittadinoDetailComponent, CittadinoUpdateComponent, CittadinoDeleteDialogComponent],
  entryComponents: [CittadinoDeleteDialogComponent],
})
export class CittadinoModule {}
