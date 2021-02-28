import * as dayjs from 'dayjs';
import { IGestore } from 'app/entities/gestore/gestore.model';
import { ITessera } from 'app/entities/tessera/tessera.model';
import { TipoCarburante } from 'app/entities/enumerations/tipo-carburante.model';

export interface IRifornimento {
  id?: number;
  data?: dayjs.Dayjs;
  progressivo?: number;
  litriErogati?: number;
  sconto?: number;
  prezzoAlLitro?: number;
  tipoCarburante?: TipoCarburante;
  gestore?: IGestore | null;
  tessera?: ITessera | null;
}

export class Rifornimento implements IRifornimento {
  constructor(
    public id?: number,
    public data?: dayjs.Dayjs,
    public progressivo?: number,
    public litriErogati?: number,
    public sconto?: number,
    public prezzoAlLitro?: number,
    public tipoCarburante?: TipoCarburante,
    public gestore?: IGestore | null,
    public tessera?: ITessera | null
  ) {}
}
