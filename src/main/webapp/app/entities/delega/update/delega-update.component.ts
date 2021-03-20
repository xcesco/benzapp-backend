import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDelega, Delega } from '../delega.model';
import { DelegaService } from '../service/delega.service';
import { ICittadino } from 'app/entities/cittadino/cittadino.model';
import { CittadinoService } from 'app/entities/cittadino/service/cittadino.service';
import { ITessera } from 'app/entities/tessera/tessera.model';
import { TesseraService } from 'app/entities/tessera/service/tessera.service';

@Component({
  selector: 'jhi-delega-update',
  templateUrl: './delega-update.component.html',
})
export class DelegaUpdateComponent implements OnInit {
  isSaving = false;
  cittadinos: ICittadino[] = [];
  tesseras: ITessera[] = [];

  editForm = this.fb.group({
    id: [],
    cittadino: [],
    tessera: [],
  });

  constructor(
    protected delegaService: DelegaService,
    protected cittadinoService: CittadinoService,
    protected tesseraService: TesseraService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ delega }) => {
      this.updateForm(delega);

      this.cittadinoService.query().subscribe((res: HttpResponse<ICittadino[]>) => (this.cittadinos = res.body ?? []));

      this.tesseraService.query().subscribe((res: HttpResponse<ITessera[]>) => (this.tesseras = res.body ?? []));
    });
  }

  updateForm(delega: IDelega): void {
    this.editForm.patchValue({
      id: delega.id,
      cittadino: delega.cittadino,
      tessera: delega.tessera,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const delega = this.createFromForm();
    if (delega.id !== undefined) {
      this.subscribeToSaveResponse(this.delegaService.update(delega));
    } else {
      this.subscribeToSaveResponse(this.delegaService.create(delega));
    }
  }

  private createFromForm(): IDelega {
    return {
      ...new Delega(),
      id: this.editForm.get(['id'])!.value,
      cittadino: this.editForm.get(['cittadino'])!.value,
      tessera: this.editForm.get(['tessera'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDelega>>): void {
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

  trackCittadinoById(index: number, item: ICittadino): number {
    return item.id!;
  }

  trackTesseraById(index: number, item: ITessera): number {
    return item.id!;
  }
}
