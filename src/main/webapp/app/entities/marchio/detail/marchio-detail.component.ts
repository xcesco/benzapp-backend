import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMarchio } from '../marchio.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-marchio-detail',
  templateUrl: './marchio-detail.component.html',
})
export class MarchioDetailComponent implements OnInit {
  marchio: IMarchio | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marchio }) => {
      this.marchio = marchio;
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
