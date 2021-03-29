import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, of } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITessera } from '../tessera.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { TesseraService } from '../service/tessera.service';
import { TesseraDeleteDialogComponent } from '../delete/tessera-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { DelegaService } from 'app/entities/delega/service/delega.service';
import { IDelega } from 'app/entities/delega/delega.model';
import { map } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-tessera',
  templateUrl: './tessera.component.html',
})
export class TesseraComponent implements OnInit {
  tesseras?: ITessera[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  deleghe: ITessera[] | null = null;

  constructor(
    protected tesseraService: TesseraService,
    protected delegheService: DelegaService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.tesseraService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ITessera[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();

    if (this.accountService.hasAnyAuthority(['ROLE_USER'])) {
      this.delegheService.query().subscribe(response => {
        console.error(response.body);
        const values = response.body;

        if (values) {
          const deleghe: ITessera[] = [];
          values.forEach(item => {
            if (item.tessera) {
              deleghe.push(item.tessera);
            }
          });
          this.deleghe = deleghe;
        }
      });
    } else {
      this.deleghe = [];
    }
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  trackId(index: number, item: ITessera): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(tessera: ITessera): void {
    const modalRef = this.modalService.open(TesseraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tessera = tessera;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ITessera[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/tessera'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.tesseras = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
