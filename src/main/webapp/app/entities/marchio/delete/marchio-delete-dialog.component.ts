import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMarchio } from '../marchio.model';
import { MarchioService } from '../service/marchio.service';

@Component({
  templateUrl: './marchio-delete-dialog.component.html',
})
export class MarchioDeleteDialogComponent {
  marchio?: IMarchio;

  constructor(protected marchioService: MarchioService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.marchioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
