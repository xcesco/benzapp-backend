import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDevice, Device } from '../device.model';
import { DeviceService } from '../service/device.service';

@Component({
  selector: 'jhi-device-update',
  templateUrl: './device-update.component.html',
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    owner: [null, [Validators.required]],
    deviceId: [null, [Validators.required]],
  });

  constructor(protected deviceService: DeviceService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.updateForm(device);
    });
  }

  updateForm(device: IDevice): void {
    this.editForm.patchValue({
      id: device.id,
      owner: device.owner,
      deviceId: device.deviceId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const device = this.createFromForm();
    if (device.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  private createFromForm(): IDevice {
    return {
      ...new Device(),
      id: this.editForm.get(['id'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      deviceId: this.editForm.get(['deviceId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
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
