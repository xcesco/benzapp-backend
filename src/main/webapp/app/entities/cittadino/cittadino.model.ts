import { ITessera } from 'app/entities/tessera/tessera.model';
import { IDelega } from 'app/entities/delega/delega.model';

export interface ICittadino {
  id?: number;
  nome?: string | null;
  cognome?: string | null;
  codiceFiscale?: string | null;
  tesseras?: ITessera[] | null;
  delegas?: IDelega[] | null;
}

export class Cittadino implements ICittadino {
  constructor(
    public id?: number,
    public nome?: string | null,
    public cognome?: string | null,
    public codiceFiscale?: string | null,
    public tesseras?: ITessera[] | null,
    public delegas?: IDelega[] | null
  ) {}
}
