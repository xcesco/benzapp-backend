import { IRifornimento } from 'app/entities/rifornimento/rifornimento.model';

export interface IGestore {
  id?: number;
  provincia?: string | null;
  comune?: string | null;
  indirizzo?: string | null;
  longitudine?: number | null;
  latitudine?: number | null;
  marchio?: string | null;
  rifornimentos?: IRifornimento[] | null;
}

export class Gestore implements IGestore {
  constructor(
    public id?: number,
    public provincia?: string | null,
    public comune?: string | null,
    public indirizzo?: string | null,
    public longitudine?: number | null,
    public latitudine?: number | null,
    public marchio?: string | null,
    public rifornimentos?: IRifornimento[] | null
  ) {}
}
