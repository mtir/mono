import { IMatch } from 'app/shared/model/match.model';

export interface IStade {
  id?: number;
  nomS?: string;
  lieu?: string;
  match?: IMatch;
}

export class Stade implements IStade {
  constructor(public id?: number, public nomS?: string, public lieu?: string, public match?: IMatch) {}
}
