import { IDelega } from 'app/entities/delega/delega.model';
import { ITessera } from 'app/entities/tessera/tessera.model';
import { IFascia } from 'app/entities/fascia/fascia.model';

export interface ICittadino {
  id?: number;
  nome?: string | null;
  cognome?: string | null;
  codiceFiscale?: string | null;
  delegas?: IDelega[] | null;
  tesseras?: ITessera[] | null;
  fascia?: IFascia | null;
}

export class Cittadino implements ICittadino {
  constructor(
    public id?: number,
    public nome?: string | null,
    public cognome?: string | null,
    public codiceFiscale?: string | null,
    public delegas?: IDelega[] | null,
    public tesseras?: ITessera[] | null,
    public fascia?: IFascia | null
  ) {}
}
