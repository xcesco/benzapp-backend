import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDelega } from '../delega.model';

@Component({
  selector: 'jhi-delega-detail',
  templateUrl: './delega-detail.component.html',
})
export class DelegaDetailComponent implements OnInit {
  delega: IDelega | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ delega }) => {
      this.delega = delega;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
