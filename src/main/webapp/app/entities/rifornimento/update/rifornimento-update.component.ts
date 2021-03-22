import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IQRCode, IRifornimento, QRReaderStatus, Rifornimento } from '../rifornimento.model';
import { RifornimentoService } from '../service/rifornimento.service';
import { IGestore } from 'app/entities/gestore/gestore.model';
import { GestoreService } from 'app/entities/gestore/service/gestore.service';
import { ITessera } from 'app/entities/tessera/tessera.model';
import { TesseraService } from 'app/entities/tessera/service/tessera.service';
import { CittadinoService } from 'app/entities/cittadino/service/cittadino.service';
import { TipoCarburante } from 'app/entities/enumerations/tipo-carburante.model';

@Component({
  selector: 'jhi-rifornimento-update',
  templateUrl: './rifornimento-update.component.html',
})
export class RifornimentoUpdateComponent implements OnInit {
  buffer = '';
  bufferStatus = QRReaderStatus.INACTIVE;
  importoDovuto: number | null = null;
  isSaving = false;
  gestores: IGestore[] = [];
  tesseras: ITessera[] = [];
  currentGestore: IGestore | null = null;
  currentTessera: ITessera | null = null;

  editForm = this.fb.group({
    id: [],
    data: [null, [Validators.required]],
    litriErogati: [null, [Validators.required]],
    sconto: [null, [Validators.required]],
    prezzoAlLitro: [null, [Validators.required]],
    tipoCarburante: [null, [Validators.required]],
    gestore: [],
    tessera: [],
  });

  constructor(
    protected rifornimentoService: RifornimentoService,
    protected gestoreService: GestoreService,
    protected tesseraService: TesseraService,
    protected cittadinoService: CittadinoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rifornimento }) => {
      if (rifornimento.id === undefined) {
        const today = dayjs(new Date());
        rifornimento.data = today;
      }

      this.updateForm(rifornimento);

      this.gestoreService.query().subscribe((res: HttpResponse<IGestore[]>) => {
        this.gestores = res.body ?? [];
        this.currentGestore = this.gestores[0];
        this.editForm.get('gestore')?.setValue(this.currentGestore);
      });
      this.tesseraService.query().subscribe((res: HttpResponse<ITessera[]>) => (this.tesseras = res.body ?? []));
    });
  }

  updateForm(rifornimento: IRifornimento): void {
    this.editForm.patchValue({
      id: rifornimento.id,
      data: rifornimento.data ? rifornimento.data.format(DATE_TIME_FORMAT) : null,
      litriErogati: rifornimento.litriErogati ?? undefined,
      sconto: rifornimento.sconto,
      prezzoAlLitro: rifornimento.prezzoAlLitro,
      tipoCarburante: rifornimento.tipoCarburante,
      gestore: rifornimento.gestore,
      tessera: rifornimento.tessera,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rifornimento = this.createFromForm();
    if (rifornimento.id !== undefined) {
      this.subscribeToSaveResponse(this.rifornimentoService.update(rifornimento));
    } else {
      this.subscribeToSaveResponse(this.rifornimentoService.create(rifornimento));
    }
  }

  private createFromForm(): IRifornimento {
    return {
      ...new Rifornimento(),
      id: this.editForm.get(['id'])!.value,
      data: this.editForm.get(['data'])!.value ? dayjs(this.editForm.get(['data'])!.value, DATE_TIME_FORMAT) : undefined,
      litriErogati: this.editForm.get(['litriErogati'])!.value,
      sconto: this.editForm.get(['sconto'])!.value,
      prezzoAlLitro: this.editForm.get(['prezzoAlLitro'])!.value,
      tipoCarburante: this.editForm.get(['tipoCarburante'])!.value,
      gestore: this.editForm.get(['gestore'])!.value,
      tessera: this.editForm.get(['tessera'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRifornimento>>): void {
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

  trackGestoreById(index: number, item: IGestore): number {
    return item.id!;
  }

  trackTesseraById(index: number, item: ITessera): number {
    return item.id!;
  }

  onQRCodeCompleted(qrcodeContent: string): void {
    console.error(qrcodeContent);
    const qrcode: IQRCode = JSON.parse(qrcodeContent);

    this.tesseraService.query({ 'codice.equals': qrcode.tesseraNumero }).subscribe(result => {
      if (result.body) {
        const value: ITessera = result.body[0];
        const currentSconto: number =
          (value.carburante === TipoCarburante.BENZINA ? value.cittadino?.fascia?.scontoBenzina : value.cittadino?.fascia?.scontoGasolio) ??
          0;

        const currentPrezzoAlLitro: number =
          (value.carburante === TipoCarburante.BENZINA
            ? this.currentGestore?.benzinaPrezzoAlLitro
            : this.currentGestore?.gasolioPrezzoAlLitro) ?? 0;

        const rifornimento: IRifornimento = {
          tipoCarburante: value.carburante,
          tessera: value,
          data: dayjs(new Date()),
          gestore: this.currentGestore,
          sconto: currentSconto,
          litriErogati: this.editForm.get('litriErogati')?.value,
          prezzoAlLitro: currentPrezzoAlLitro,
        };

        this.updateForm(rifornimento);
        this.onChangeLitriErogati();
      }
    });
    console.error(qrcode);
  }

  onReadQRCode(
    $event: KeyboardEvent,
    qrinfo_stop: HTMLDivElement,
    qrinfo_run: HTMLDivElement,
    qrinfo_spinner: HTMLDivElement,
    litriErogati: HTMLInputElement
  ): void {
    if ($event.key === '{') {
      // avvio
      qrinfo_stop.hidden = true;
      qrinfo_run.hidden = true;
      qrinfo_spinner.hidden = false;

      this.buffer = '';
      this.bufferStatus = QRReaderStatus.RUNNING;
    }

    if (this.bufferStatus === QRReaderStatus.RUNNING) {
      this.buffer += $event.key;

      if ($event.key === '}') {
        qrinfo_stop.hidden = false;
        qrinfo_run.hidden = true;
        qrinfo_spinner.hidden = true;

        console.error(this.buffer);

        this.onQRCodeCompleted(this.buffer);
        this.bufferStatus = QRReaderStatus.FINISHED;
        // litriErogati.focus();
      }
    }
  }

  onFocusIn(): void {
    this.bufferStatus = QRReaderStatus.READY;
  }

  onFocusOut(): void {
    this.bufferStatus = QRReaderStatus.INACTIVE;
  }

  getInfoQRCodeButton(): string {
    switch (this.bufferStatus) {
      case QRReaderStatus.FINISHED:
        return 'Lettura QRCode terminata';
        break;
      case QRReaderStatus.INACTIVE:
        return 'Seleziona questo riquadro per avviare lettura QRCode';
        break;
      case QRReaderStatus.RUNNING:
        return 'Attendere prego. Non deselezionare il riquadro.';
        break;
      case QRReaderStatus.READY:
        return 'In attesa di QRCode';
        break;
    }
    return '';
  }

  onChangeLitriErogati(): void {
    this.importoDovuto =
      (this.editForm.get('prezzoAlLitro')?.value - this.editForm.get('sconto')?.value) * this.editForm.get('litriErogati')?.value;
  }
}
