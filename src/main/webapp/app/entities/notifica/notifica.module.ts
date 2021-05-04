import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NotificaComponent } from './list/notifica.component';
import { NotificaDetailComponent } from './detail/notifica-detail.component';
import { NotificaUpdateComponent } from './update/notifica-update.component';
import { NotificaDeleteDialogComponent } from './delete/notifica-delete-dialog.component';
import { NotificaRoutingModule } from './route/notifica-routing.module';

@NgModule({
  imports: [SharedModule, NotificaRoutingModule],
  declarations: [NotificaComponent, NotificaDetailComponent, NotificaUpdateComponent, NotificaDeleteDialogComponent],
  entryComponents: [NotificaDeleteDialogComponent],
})
export class NotificaModule {}
