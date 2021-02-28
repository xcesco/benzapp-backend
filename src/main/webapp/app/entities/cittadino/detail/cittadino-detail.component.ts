import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICittadino } from '../cittadino.model';

@Component({
  selector: 'jhi-cittadino-detail',
  templateUrl: './cittadino-detail.component.html',
})
export class CittadinoDetailComponent implements OnInit {
  cittadino: ICittadino | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cittadino }) => {
      this.cittadino = cittadino;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
