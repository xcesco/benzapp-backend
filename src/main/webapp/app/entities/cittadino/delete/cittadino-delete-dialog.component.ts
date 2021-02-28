import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICittadino } from '../cittadino.model';
import { CittadinoService } from '../service/cittadino.service';

@Component({
  templateUrl: './cittadino-delete-dialog.component.html',
})
export class CittadinoDeleteDialogComponent {
  cittadino?: ICittadino;

  constructor(protected cittadinoService: CittadinoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cittadinoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
