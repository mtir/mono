import { Moment } from 'moment';
import { IJoueur } from 'app/shared/model/joueur.model';

export interface IMatch {
  id?: number;
  nomM?: string;
  numM?: number;
  dureeM?: number;
  dateM?: Moment;
  joueurs?: IJoueur[];
}

export class Match implements IMatch {
  constructor(
    public id?: number,
    public nomM?: string,
    public numM?: number,
    public dureeM?: number,
    public dateM?: Moment,
    public joueurs?: IJoueur[]
  ) {}
}
