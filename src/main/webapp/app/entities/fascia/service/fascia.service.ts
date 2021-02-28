import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/core/request/request-util';
import { IFascia } from '../fascia.model';

type EntityResponseType = HttpResponse<IFascia>;
type EntityArrayResponseType = HttpResponse<IFascia[]>;

@Injectable({ providedIn: 'root' })
export class FasciaService {
  public resourceUrl = SERVER_API_URL + 'api/fascias';

  constructor(protected http: HttpClient) {}

  create(fascia: IFascia): Observable<EntityResponseType> {
    return this.http.post<IFascia>(this.resourceUrl, fascia, { observe: 'response' });
  }

  update(fascia: IFascia): Observable<EntityResponseType> {
    return this.http.put<IFascia>(this.resourceUrl, fascia, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFascia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFascia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
