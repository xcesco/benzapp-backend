import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IGestore, Gestore } from '../gestore.model';
import { GestoreService } from '../service/gestore.service';

@Component({
  selector: 'jhi-gestore-update',
  templateUrl: './gestore-update.component.html',
})
export class GestoreUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    provincia: [],
    comune: [],
    indirizzo: [],
    longitudine: [],
    latitudine: [],
    marchio: [],
  });

  constructor(protected gestoreService: GestoreService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestore }) => {
      this.updateForm(gestore);
    });
  }

  updateForm(gestore: IGestore): void {
    this.editForm.patchValue({
      id: gestore.id,
      provincia: gestore.provincia,
      comune: gestore.comune,
      indirizzo: gestore.indirizzo,
      longitudine: gestore.longitudine,
      latitudine: gestore.latitudine,
      marchio: gestore.marchio,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gestore = this.createFromForm();
    if (gestore.id !== undefined) {
      this.subscribeToSaveResponse(this.gestoreService.update(gestore));
    } else {
      this.subscribeToSaveResponse(this.gestoreService.create(gestore));
    }
  }

  private createFromForm(): IGestore {
    return {
      ...new Gestore(),
      id: this.editForm.get(['id'])!.value,
      provincia: this.editForm.get(['provincia'])!.value,
      comune: this.editForm.get(['comune'])!.value,
      indirizzo: this.editForm.get(['indirizzo'])!.value,
      longitudine: this.editForm.get(['longitudine'])!.value,
      latitudine: this.editForm.get(['latitudine'])!.value,
      marchio: this.editForm.get(['marchio'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGestore>>): void {
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
