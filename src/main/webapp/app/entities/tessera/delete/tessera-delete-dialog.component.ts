import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITessera } from '../tessera.model';
import { TesseraService } from '../service/tessera.service';

@Component({
  templateUrl: './tessera-delete-dialog.component.html',
})
export class TesseraDeleteDialogComponent {
  tessera?: ITessera;

  constructor(protected tesseraService: TesseraService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tesseraService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
