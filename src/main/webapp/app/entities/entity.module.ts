import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'match',
        loadChildren: () => import('./match/match.module').then(m => m.MywebMatchModule),
      },
      {
        path: 'joueur',
        loadChildren: () => import('./joueur/joueur.module').then(m => m.MywebJoueurModule),
      },
      {
        path: 'stade',
        loadChildren: () => import('./stade/stade.module').then(m => m.MywebStadeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class MywebEntityModule {}
