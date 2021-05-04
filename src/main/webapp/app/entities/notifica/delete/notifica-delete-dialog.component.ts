import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INotifica } from '../notifica.model';
import { NotificaService } from '../service/notifica.service';

@Component({
  templateUrl: './notifica-delete-dialog.component.html',
})
export class NotificaDeleteDialogComponent {
  notifica?: INotifica;

  constructor(protected notificaService: NotificaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
