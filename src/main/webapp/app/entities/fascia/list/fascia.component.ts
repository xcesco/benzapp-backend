import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFascia } from '../fascia.model';
import { FasciaService } from '../service/fascia.service';
import { FasciaDeleteDialogComponent } from '../delete/fascia-delete-dialog.component';

@Component({
  selector: 'jhi-fascia',
  templateUrl: './fascia.component.html',
})
export class FasciaComponent implements OnInit {
  fascias?: IFascia[];
  isLoading = false;

  constructor(protected fasciaService: FasciaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fasciaService.query().subscribe(
      (res: HttpResponse<IFascia[]>) => {
        this.isLoading = false;
        this.fascias = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFascia): number {
    return item.id!;
  }

  delete(fascia: IFascia): void {
    const modalRef = this.modalService.open(FasciaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fascia = fascia;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
