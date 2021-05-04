import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotifica } from '../notifica.model';

@Component({
  selector: 'jhi-notifica-detail',
  templateUrl: './notifica-detail.component.html',
})
export class NotificaDetailComponent implements OnInit {
  notifica: INotifica | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notifica }) => {
      this.notifica = notifica;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
