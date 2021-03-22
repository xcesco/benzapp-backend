import { ICittadino } from 'app/entities/cittadino/cittadino.model';

export interface IFascia {
  id?: number;
  descrizione?: string | null;
  scontoBenzina?: number | null;
  scontoGasolio?: number | null;
  cittadinos?: ICittadino[] | null;
}

export class Fascia implements IFascia {
  constructor(
    public id?: number,
    public descrizione?: string | null,
    public scontoBenzina?: number | null,
    public scontoGasolio?: number | null,
    public cittadinos?: ICittadino[] | null
  ) {}
}
