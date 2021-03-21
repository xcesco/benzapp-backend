import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IQRCode, IRifornimento, Rifornimento } from '../rifornimento.model';
import { RifornimentoService } from '../service/rifornimento.service';
import { IGestore } from 'app/entities/gestore/gestore.model';
import { GestoreService } from 'app/entities/gestore/service/gestore.service';
import { ITessera } from 'app/entities/tessera/tessera.model';
import { TesseraService } from 'app/entities/tessera/service/tessera.service';
import { CittadinoService } from 'app/entities/cittadino/service/cittadino.service';
import { TipoCarburante } from 'app/entities/enumerations/tipo-carburante.model';
import { Dayjs } from 'dayjs';

@Component({
  selector: 'jhi-rifornimento-update',
  templateUrl: './rifornimento-update.component.html',
})
export class RifornimentoUpdateComponent implements OnInit {
  buffer = '';

  isSaving = false;
  gestores: IGestore[] = [];
  tesseras: ITessera[] = [];

  currentTessera: ITessera | null = null;

  editForm = this.fb.group({
    id: [],
    data: [null, [Validators.required]],
    progressivo: [null, [Validators.required]],
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

  // @HostListener('document:keypress', ['$event'])
  // handleKeyboardEvent(event: KeyboardEvent) {
  //   // this.key = event.key;
  // }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rifornimento }) => {
      if (rifornimento.id === undefined) {
        const today = dayjs().startOf('day');
        rifornimento.data = today;
      }

      this.updateForm(rifornimento);

      this.gestoreService.query().subscribe((res: HttpResponse<IGestore[]>) => (this.gestores = res.body ?? []));

      this.tesseraService.query().subscribe((res: HttpResponse<ITessera[]>) => (this.tesseras = res.body ?? []));
    });
  }

  updateForm(rifornimento: IRifornimento): void {
    this.editForm.patchValue({
      id: rifornimento.id,
      data: rifornimento.data ? rifornimento.data.format(DATE_TIME_FORMAT) : null,
      progressivo: rifornimento.progressivo,
      litriErogati: rifornimento.litriErogati,
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
      progressivo: this.editForm.get(['progressivo'])!.value,
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

  onChangeQRCode($event: any): void {
    console.error($event.target.value);
    const qrcode: IQRCode = JSON.parse($event.target.value);

    this.tesseraService.query({ 'codice.equals': qrcode.tesseraNumero }).subscribe(result => {
      if (result.body) {
        const value: ITessera = result.body[0];

        const rifornimento: IRifornimento = {
          tipoCarburante: value.carburante,
          tessera: value,
          data: dayjs(new Date()),
        };
        alert(rifornimento.tessera);
        this.updateForm(rifornimento);
      }
    });
    // this.cittadinoService.query({'codiceFiscale.equals': qrcode.codiceFiscale}).subscribe(result => {
    //   if (result.body) {
    //     alert(result.body[0].cognome);
    //
    //     const rifornimento: IRifornimento = {tipoCarburante: TipoCarburante.BENZINA};
    //     this.updateForm(rifornimento);
    //   }
    //
    // })
    console.error(qrcode);
  }
}
