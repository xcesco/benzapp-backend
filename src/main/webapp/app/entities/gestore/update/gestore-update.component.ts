import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IGestore, Gestore } from '../gestore.model';
import { GestoreService } from '../service/gestore.service';
import { IFascia } from 'app/entities/fascia/fascia.model';
import { FasciaService } from 'app/entities/fascia/service/fascia.service';
import { IMarchio } from 'app/entities/marchio/marchio.model';
import { MarchioService } from 'app/entities/marchio/service/marchio.service';

@Component({
  selector: 'jhi-gestore-update',
  templateUrl: './gestore-update.component.html',
})
export class GestoreUpdateComponent implements OnInit {
  isSaving = false;
  fascias: IFascia[] = [];
  marchios: IMarchio[] = [];

  editForm = this.fb.group({
    id: [],
    provincia: [],
    comune: [],
    indirizzo: [],
    longitudine: [],
    latitudine: [],
    tipo: [],
    benzinaPrezzoAlLitro: [],
    gasolioPrezzoAlLitro: [],
    fascia: [],
    marchio: [],
  });

  constructor(
    protected gestoreService: GestoreService,
    protected fasciaService: FasciaService,
    protected marchioService: MarchioService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestore }) => {
      this.updateForm(gestore);

      this.fasciaService.query().subscribe((res: HttpResponse<IFascia[]>) => (this.fascias = res.body ?? []));

      this.marchioService.query().subscribe((res: HttpResponse<IMarchio[]>) => (this.marchios = res.body ?? []));
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
      tipo: gestore.tipo,
      benzinaPrezzoAlLitro: gestore.benzinaPrezzoAlLitro,
      gasolioPrezzoAlLitro: gestore.gasolioPrezzoAlLitro,
      fascia: gestore.fascia,
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
      tipo: this.editForm.get(['tipo'])!.value,
      benzinaPrezzoAlLitro: this.editForm.get(['benzinaPrezzoAlLitro'])!.value,
      gasolioPrezzoAlLitro: this.editForm.get(['gasolioPrezzoAlLitro'])!.value,
      fascia: this.editForm.get(['fascia'])!.value,
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

  trackFasciaById(index: number, item: IFascia): number {
    return item.id!;
  }

  trackMarchioById(index: number, item: IMarchio): number {
    return item.id!;
  }
}
