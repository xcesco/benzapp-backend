import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRifornimento } from '../rifornimento.model';

@Component({
  selector: 'jhi-rifornimento-detail',
  templateUrl: './rifornimento-detail.component.html',
})
export class RifornimentoDetailComponent implements OnInit {
  rifornimento: IRifornimento | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rifornimento }) => {
      this.rifornimento = rifornimento;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
