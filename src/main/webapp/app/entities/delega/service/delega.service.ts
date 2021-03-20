import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/core/request/request-util';
import { IDelega } from '../delega.model';

type EntityResponseType = HttpResponse<IDelega>;
type EntityArrayResponseType = HttpResponse<IDelega[]>;

@Injectable({ providedIn: 'root' })
export class DelegaService {
  public resourceUrl = SERVER_API_URL + 'api/delegas';

  constructor(protected http: HttpClient) {}

  create(delega: IDelega): Observable<EntityResponseType> {
    return this.http.post<IDelega>(this.resourceUrl, delega, { observe: 'response' });
  }

  update(delega: IDelega): Observable<EntityResponseType> {
    return this.http.put<IDelega>(this.resourceUrl, delega, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDelega>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDelega[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
