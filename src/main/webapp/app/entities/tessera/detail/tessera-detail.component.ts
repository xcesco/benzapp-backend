import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITessera } from '../tessera.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tessera-detail',
  templateUrl: './tessera-detail.component.html',
})
export class TesseraDetailComponent implements OnInit {
  tessera: ITessera | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tessera }) => {
      this.tessera = tessera;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
