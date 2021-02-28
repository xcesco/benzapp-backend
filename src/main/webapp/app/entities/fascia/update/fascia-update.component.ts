import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFascia, Fascia } from '../fascia.model';
import { FasciaService } from '../service/fascia.service';

@Component({
  selector: 'jhi-fascia-update',
  templateUrl: './fascia-update.component.html',
})
export class FasciaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descrizione: [],
    scontoBenzina: [],
    scontoGasolio: [],
  });

  constructor(protected fasciaService: FasciaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fascia }) => {
      this.updateForm(fascia);
    });
  }

  updateForm(fascia: IFascia): void {
    this.editForm.patchValue({
      id: fascia.id,
      descrizione: fascia.descrizione,
      scontoBenzina: fascia.scontoBenzina,
      scontoGasolio: fascia.scontoGasolio,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fascia = this.createFromForm();
    if (fascia.id !== undefined) {
      this.subscribeToSaveResponse(this.fasciaService.update(fascia));
    } else {
      this.subscribeToSaveResponse(this.fasciaService.create(fascia));
    }
  }

  private createFromForm(): IFascia {
    return {
      ...new Fascia(),
      id: this.editForm.get(['id'])!.value,
      descrizione: this.editForm.get(['descrizione'])!.value,
      scontoBenzina: this.editForm.get(['scontoBenzina'])!.value,
      scontoGasolio: this.editForm.get(['scontoGasolio'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFascia>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
