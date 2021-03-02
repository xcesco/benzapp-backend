import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITessera, Tessera } from '../tessera.model';
import { TesseraService } from '../service/tessera.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICittadino } from 'app/entities/cittadino/cittadino.model';
import { CittadinoService } from 'app/entities/cittadino/service/cittadino.service';

@Component({
  selector: 'jhi-tessera-update',
  templateUrl: './tessera-update.component.html',
})
export class TesseraUpdateComponent implements OnInit {
  isSaving = false;
  cittadinos: ICittadino[] = [];

  editForm = this.fb.group({
    id: [],
    codice: [null, [Validators.required]],
    dataEmissione: [null, [Validators.required]],
    immagine: [],
    immagineContentType: [],
    targa: [null, [Validators.required]],
    veicolo: [null, [Validators.required]],
    carburante: [null, [Validators.required]],
    cittadino: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected tesseraService: TesseraService,
    protected cittadinoService: CittadinoService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tessera }) => {
      if (tessera.id === undefined) {
        const today = dayjs().startOf('day');
        tessera.dataEmissione = today;
      }

      this.updateForm(tessera);

      this.cittadinoService.query().subscribe((res: HttpResponse<ICittadino[]>) => (this.cittadinos = res.body ?? []));
    });
  }

  updateForm(tessera: ITessera): void {
    this.editForm.patchValue({
      id: tessera.id,
      codice: tessera.codice,
      dataEmissione: tessera.dataEmissione ? tessera.dataEmissione.format(DATE_TIME_FORMAT) : null,
      immagine: tessera.immagine,
      immagineContentType: tessera.immagineContentType,
      targa: tessera.targa,
      veicolo: tessera.veicolo,
      carburante: tessera.carburante,
      cittadino: tessera.cittadino,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('benzappApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tessera = this.createFromForm();
    if (tessera.id !== undefined) {
      this.subscribeToSaveResponse(this.tesseraService.update(tessera));
    } else {
      this.subscribeToSaveResponse(this.tesseraService.create(tessera));
    }
  }

  private createFromForm(): ITessera {
    return {
      ...new Tessera(),
      id: this.editForm.get(['id'])!.value,
      codice: this.editForm.get(['codice'])!.value,
      dataEmissione: this.editForm.get(['dataEmissione'])!.value
        ? dayjs(this.editForm.get(['dataEmissione'])!.value, DATE_TIME_FORMAT)
        : undefined,
      immagineContentType: this.editForm.get(['immagineContentType'])!.value,
      immagine: this.editForm.get(['immagine'])!.value,
      targa: this.editForm.get(['targa'])!.value,
      veicolo: this.editForm.get(['veicolo'])!.value,
      carburante: this.editForm.get(['carburante'])!.value,
      cittadino: this.editForm.get(['cittadino'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITessera>>): void {
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
}
