import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFascia } from '../fascia.model';

@Component({
  selector: 'jhi-fascia-detail',
  templateUrl: './fascia-detail.component.html',
})
export class FasciaDetailComponent implements OnInit {
  fascia: IFascia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fascia }) => {
      this.fascia = fascia;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
