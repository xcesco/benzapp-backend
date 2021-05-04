import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotifica, getNotificaIdentifier } from '../notifica.model';

export type EntityResponseType = HttpResponse<INotifica>;
export type EntityArrayResponseType = HttpResponse<INotifica[]>;

@Injectable({ providedIn: 'root' })
export class NotificaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/notificas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(notifica: INotifica): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notifica);
    return this.http
      .post<INotifica>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(notifica: INotifica): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notifica);
    return this.http
      .put<INotifica>(`${this.resourceUrl}/${getNotificaIdentifier(notifica) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(notifica: INotifica): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notifica);
    return this.http
      .patch<INotifica>(`${this.resourceUrl}/${getNotificaIdentifier(notifica) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INotifica>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  queryByTarga(targa: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<INotifica[]>(`${this.resourceUrl}?targa.equals=${targa}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INotifica[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNotificaToCollectionIfMissing(notificaCollection: INotifica[], ...notificasToCheck: (INotifica | null | undefined)[]): INotifica[] {
    const notificas: INotifica[] = notificasToCheck.filter(isPresent);
    if (notificas.length > 0) {
      const notificaCollectionIdentifiers = notificaCollection.map(notificaItem => getNotificaIdentifier(notificaItem)!);
      const notificasToAdd = notificas.filter(notificaItem => {
        const notificaIdentifier = getNotificaIdentifier(notificaItem);
        if (notificaIdentifier == null || notificaCollectionIdentifiers.includes(notificaIdentifier)) {
          return false;
        }
        notificaCollectionIdentifiers.push(notificaIdentifier);
        return true;
      });
      return [...notificasToAdd, ...notificaCollection];
    }
    return notificaCollection;
  }

  protected convertDateFromClient(notifica: INotifica): INotifica {
    return Object.assign({}, notifica, {
      data: notifica.data?.isValid() ? notifica.data.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.data = res.body.data ? dayjs(res.body.data) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((notifica: INotifica) => {
        notifica.data = notifica.data ? dayjs(notifica.data) : undefined;
      });
    }
    return res;
  }
}
