import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFascia } from '../fascia.model';
import { FasciaService } from '../service/fascia.service';

@Component({
  templateUrl: './fascia-delete-dialog.component.html',
})
export class FasciaDeleteDialogComponent {
  fascia?: IFascia;

  constructor(protected fasciaService: FasciaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fasciaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
