import * as dayjs from 'dayjs';

export interface INotifica {
  id?: number;
  targa?: string;
  data?: dayjs.Dayjs;
}

export class Notifica implements INotifica {
  constructor(public id?: number, public targa?: string, public data?: dayjs.Dayjs) {}
}

export function getNotificaIdentifier(notifica: INotifica): number | undefined {
  return notifica.id;
}
