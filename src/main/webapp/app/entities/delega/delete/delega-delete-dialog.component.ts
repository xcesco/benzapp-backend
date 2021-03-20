import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDelega } from '../delega.model';
import { DelegaService } from '../service/delega.service';

@Component({
  templateUrl: './delega-delete-dialog.component.html',
})
export class DelegaDeleteDialogComponent {
  delega?: IDelega;

  constructor(protected delegaService: DelegaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.delegaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
