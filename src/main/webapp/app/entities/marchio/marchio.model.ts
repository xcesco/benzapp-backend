import { IGestore } from 'app/entities/gestore/gestore.model';

export interface IMarchio {
  id?: number;
  immagineContentType?: string | null;
  immagine?: string | null;
  nome?: string;
  gestores?: IGestore[] | null;
}

export class Marchio implements IMarchio {
  constructor(
    public id?: number,
    public immagineContentType?: string | null,
    public immagine?: string | null,
    public nome?: string,
    public gestores?: IGestore[] | null
  ) {}
}
