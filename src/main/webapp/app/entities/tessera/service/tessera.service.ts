import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/core/request/request-util';
import { ITessera } from '../tessera.model';

type EntityResponseType = HttpResponse<ITessera>;
type EntityArrayResponseType = HttpResponse<ITessera[]>;

@Injectable({ providedIn: 'root' })
export class TesseraService {
  public resourceUrl = SERVER_API_URL + 'api/tesseras';

  constructor(protected http: HttpClient) {}

  create(tessera: ITessera): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tessera);
    return this.http
      .post<ITessera>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tessera: ITessera): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tessera);
    return this.http
      .put<ITessera>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITessera>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITessera[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(tessera: ITessera): ITessera {
    const copy: ITessera = Object.assign({}, tessera, {
      dataEmissione: tessera.dataEmissione?.isValid() ? tessera.dataEmissione.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataEmissione = res.body.dataEmissione ? dayjs(res.body.dataEmissione) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tessera: ITessera) => {
        tessera.dataEmissione = tessera.dataEmissione ? dayjs(tessera.dataEmissione) : undefined;
      });
    }
    return res;
  }
}
