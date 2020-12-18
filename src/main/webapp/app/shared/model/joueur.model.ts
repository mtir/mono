import { IMatch } from 'app/shared/model/match.model';

export interface IJoueur {
  id?: number;
  nomJ?: string;
  prenomJ?: string;
  numJ?: number;
  match?: IMatch;
}

export class Joueur implements IJoueur {
  constructor(public id?: number, public nomJ?: string, public prenomJ?: string, public numJ?: number, public match?: IMatch) {}
}
