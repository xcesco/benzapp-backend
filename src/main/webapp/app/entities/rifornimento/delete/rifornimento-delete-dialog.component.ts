import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRifornimento } from '../rifornimento.model';
import { RifornimentoService } from '../service/rifornimento.service';

@Component({
  templateUrl: './rifornimento-delete-dialog.component.html',
})
export class RifornimentoDeleteDialogComponent {
  rifornimento?: IRifornimento;

  constructor(protected rifornimentoService: RifornimentoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rifornimentoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
