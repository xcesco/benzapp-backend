import * as dayjs from 'dayjs';
import { IDelega } from 'app/entities/delega/delega.model';
import { IRifornimento } from 'app/entities/rifornimento/rifornimento.model';
import { ICittadino } from 'app/entities/cittadino/cittadino.model';
import { TipoVeicolo } from 'app/entities/enumerations/tipo-veicolo.model';
import { TipoCarburante } from 'app/entities/enumerations/tipo-carburante.model';

export interface ITessera {
  id?: number;
  codice?: string;
  dataEmissione?: dayjs.Dayjs;
  immagineContentType?: string | null;
  immagine?: string | null;
  targa?: string;
  veicolo?: TipoVeicolo;
  carburante?: TipoCarburante;
  delegas?: IDelega[] | null;
  rifornimentos?: IRifornimento[] | null;
  cittadino?: ICittadino | null;
}

export class Tessera implements ITessera {
  constructor(
    public id?: number,
    public codice?: string,
    public dataEmissione?: dayjs.Dayjs,
    public immagineContentType?: string | null,
    public immagine?: string | null,
    public targa?: string,
    public veicolo?: TipoVeicolo,
    public carburante?: TipoCarburante,
    public delegas?: IDelega[] | null,
    public rifornimentos?: IRifornimento[] | null,
    public cittadino?: ICittadino | null
  ) {}
}
