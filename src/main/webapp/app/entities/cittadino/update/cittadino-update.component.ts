import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICittadino, Cittadino } from '../cittadino.model';
import { CittadinoService } from '../service/cittadino.service';

@Component({
  selector: 'jhi-cittadino-update',
  templateUrl: './cittadino-update.component.html',
})
export class CittadinoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    cognome: [],
    codiceFiscale: [],
  });

  constructor(protected cittadinoService: CittadinoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cittadino }) => {
      this.updateForm(cittadino);
    });
  }

  updateForm(cittadino: ICittadino): void {
    this.editForm.patchValue({
      id: cittadino.id,
      nome: cittadino.nome,
      cognome: cittadino.cognome,
      codiceFiscale: cittadino.codiceFiscale,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cittadino = this.createFromForm();
    if (cittadino.id !== undefined) {
      this.subscribeToSaveResponse(this.cittadinoService.update(cittadino));
    } else {
      this.subscribeToSaveResponse(this.cittadinoService.create(cittadino));
    }
  }

  private createFromForm(): ICittadino {
    return {
      ...new Cittadino(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      cognome: this.editForm.get(['cognome'])!.value,
      codiceFiscale: this.editForm.get(['codiceFiscale'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICittadino>>): void {
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
