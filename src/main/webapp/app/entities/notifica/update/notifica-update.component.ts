import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INotifica, Notifica } from '../notifica.model';
import { NotificaService } from '../service/notifica.service';

@Component({
  selector: 'jhi-notifica-update',
  templateUrl: './notifica-update.component.html',
})
export class NotificaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    targa: [null, [Validators.required]],
    data: [null, [Validators.required]],
  });

  constructor(protected notificaService: NotificaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notifica }) => {
      if (notifica.id === undefined) {
        const today = dayjs().startOf('day');
        notifica.data = today;
      }

      this.updateForm(notifica);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notifica = this.createFromForm();
    if (notifica.id !== undefined) {
      this.subscribeToSaveResponse(this.notificaService.update(notifica));
    } else {
      this.subscribeToSaveResponse(this.notificaService.create(notifica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotifica>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(notifica: INotifica): void {
    this.editForm.patchValue({
      id: notifica.id,
      targa: notifica.targa,
      data: notifica.data ? notifica.data.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): INotifica {
    return {
      ...new Notifica(),
      id: this.editForm.get(['id'])!.value,
      targa: this.editForm.get(['targa'])!.value,
      data: this.editForm.get(['data'])!.value ? dayjs(this.editForm.get(['data'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
