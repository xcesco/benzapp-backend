import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGestore } from '../gestore.model';
import { GestoreService } from '../service/gestore.service';

@Component({
  templateUrl: './gestore-delete-dialog.component.html',
})
export class GestoreDeleteDialogComponent {
  gestore?: IGestore;

  constructor(protected gestoreService: GestoreService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gestoreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
