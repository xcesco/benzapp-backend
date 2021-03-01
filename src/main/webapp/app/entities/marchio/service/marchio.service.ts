import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/core/request/request-util';
import { IMarchio } from '../marchio.model';

type EntityResponseType = HttpResponse<IMarchio>;
type EntityArrayResponseType = HttpResponse<IMarchio[]>;

@Injectable({ providedIn: 'root' })
export class MarchioService {
  public resourceUrl = SERVER_API_URL + 'api/marchios';

  constructor(protected http: HttpClient) {}

  create(marchio: IMarchio): Observable<EntityResponseType> {
    return this.http.post<IMarchio>(this.resourceUrl, marchio, { observe: 'response' });
  }

  update(marchio: IMarchio): Observable<EntityResponseType> {
    return this.http.put<IMarchio>(this.resourceUrl, marchio, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMarchio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMarchio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
