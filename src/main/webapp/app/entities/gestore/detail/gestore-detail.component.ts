import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGestore } from '../gestore.model';

@Component({
  selector: 'jhi-gestore-detail',
  templateUrl: './gestore-detail.component.html',
})
export class GestoreDetailComponent implements OnInit {
  gestore: IGestore | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestore }) => {
      this.gestore = gestore;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
