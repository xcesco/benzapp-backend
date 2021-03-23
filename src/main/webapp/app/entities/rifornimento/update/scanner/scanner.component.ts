import { Component, Output } from '@angular/core';
import { BarcodeFormat } from '@zxing/library';
import { BehaviorSubject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'jhi-scanner',
  templateUrl: './scanner.component.html',
  styleUrls: ['./scanner.component.scss'],
})
export class ScannerComponent {
  @Output() completed = new EventEmitter<string>();

  scannerEnabled = true;
  private information = 'Nessuna informazione sul codice rilevata. Ingrandisci un codice QR per scansionarlo.';

  availableDevices: MediaDeviceInfo[] | undefined | null;
  currentDevice: MediaDeviceInfo | undefined;

  formatsEnabled: BarcodeFormat[] = [BarcodeFormat.CODE_128, BarcodeFormat.DATA_MATRIX, BarcodeFormat.EAN_13, BarcodeFormat.QR_CODE];

  hasDevices = false;
  hasPermission = false;

  qrResultString: string | null = null;

  torchEnabled = false;
  torchAvailable$ = new BehaviorSubject<boolean>(false);
  tryHarder = false;

  constructor(private modalService: NgbModal) {}

  public scanSuccessHandler($event: string): void {
    this.scannerEnabled = false;
    this.information = 'Attendi recupero informazioni ...';

    this.completed.emit($event);

    console.error($event);
  }

  public enableScanner(): void {
    this.scannerEnabled = !this.scannerEnabled;
    this.information = 'No se ha detectado información de ningún código. Acerque un código QR para escanear.';
  }

  clearResult(): void {
    this.qrResultString = null;
  }

  onCamerasFound(devices: MediaDeviceInfo[]): void {
    this.availableDevices = devices;
    this.hasDevices = true; // Boolean(devices && devices.length);
  }

  onCodeResult(resultString: string): void {
    this.qrResultString = resultString;
  }

  onDeviceSelectChange($event: Event): void {
    const selected: any = $event.target;
    const device = this.availableDevices?.find(x => x.deviceId === selected.value);
    this.currentDevice = device ?? undefined;
  }

  // openFormatsDialog() {
  //   const data = {
  //     formatsEnabled: this.formatsEnabled,
  //   };
  //
  //   this._dialog
  //     .open(FormatsDialogComponent, {data})
  //     .afterClosed()
  //     .subscribe(x => {
  //       if (x) {
  //         this.formatsEnabled = x;
  //       }
  //     });
  // }

  onHasPermission(has: boolean): void {
    this.hasPermission = has;
  }

  // openInfoDialog() {
  //   const data = {
  //     hasDevices: this.hasDevices,
  //     hasPermission: this.hasPermission,
  //   };
  //
  //   this._dialog.open(AppInfoDialogComponent, {data});
  // }

  onTorchCompatible(isCompatible: boolean): void {
    this.torchAvailable$.next(isCompatible || false);
  }

  toggleTorch(): void {
    this.torchEnabled = !this.torchEnabled;
  }

  toggleTryHarder(): void {
    this.tryHarder = !this.tryHarder;
  }
}
