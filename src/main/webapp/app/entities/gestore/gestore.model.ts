import { IRifornimento } from 'app/entities/rifornimento/rifornimento.model';
import { IMarchio } from 'app/entities/marchio/marchio.model';
import { TipoImpianto } from 'app/entities/enumerations/tipo-impianto.model';

export interface IGestore {
  id?: number;
  provincia?: string | null;
  comune?: string | null;
  indirizzo?: string | null;
  longitudine?: number | null;
  latitudine?: number | null;
  tipo?: TipoImpianto | null;
  benzinaPrezzoAlLitro?: number | null;
  gasolioPrezzoAlLitro?: number | null;
  owner?: string | null;
  rifornimentos?: IRifornimento[] | null;
  marchio?: IMarchio | null;
}

export class Gestore implements IGestore {
  constructor(
    public id?: number,
    public provincia?: string | null,
    public comune?: string | null,
    public indirizzo?: string | null,
    public longitudine?: number | null,
    public latitudine?: number | null,
    public tipo?: TipoImpianto | null,
    public benzinaPrezzoAlLitro?: number | null,
    public gasolioPrezzoAlLitro?: number | null,
    public owner?: string | null,
    public rifornimentos?: IRifornimento[] | null,
    public marchio?: IMarchio | null
  ) {}
}
