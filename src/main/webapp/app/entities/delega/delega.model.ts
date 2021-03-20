import { ICittadino } from 'app/entities/cittadino/cittadino.model';
import { ITessera } from 'app/entities/tessera/tessera.model';

export interface IDelega {
  id?: number;
  cittadino?: ICittadino | null;
  tessera?: ITessera | null;
}

export class Delega implements IDelega {
  constructor(public id?: number, public cittadino?: ICittadino | null, public tessera?: ITessera | null) {}
}
