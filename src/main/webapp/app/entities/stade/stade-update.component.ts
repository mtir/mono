import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IStade, Stade } from 'app/shared/model/stade.model';
import { StadeService } from './stade.service';
import { IMatch } from 'app/shared/model/match.model';
import { MatchService } from 'app/entities/match/match.service';

@Component({
  selector: 'jhi-stade-update',
  templateUrl: './stade-update.component.html',
})
export class StadeUpdateComponent implements OnInit {
  isSaving = false;
  matches: IMatch[] = [];

  editForm = this.fb.group({
    id: [],
    nomS: [],
    lieu: [],
    match: [],
  });

  constructor(
    protected stadeService: StadeService,
    protected matchService: MatchService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stade }) => {
      this.updateForm(stade);

      this.matchService
        .query({ filter: 'stade-is-null' })
        .pipe(
          map((res: HttpResponse<IMatch[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IMatch[]) => {
          if (!stade.match || !stade.match.id) {
            this.matches = resBody;
          } else {
            this.matchService
              .find(stade.match.id)
              .pipe(
                map((subRes: HttpResponse<IMatch>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IMatch[]) => (this.matches = concatRes));
          }
        });
    });
  }

  updateForm(stade: IStade): void {
    this.editForm.patchValue({
      id: stade.id,
      nomS: stade.nomS,
      lieu: stade.lieu,
      match: stade.match,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stade = this.createFromForm();
    if (stade.id !== undefined) {
      this.subscribeToSaveResponse(this.stadeService.update(stade));
    } else {
      this.subscribeToSaveResponse(this.stadeService.create(stade));
    }
  }

  private createFromForm(): IStade {
    return {
      ...new Stade(),
      id: this.editForm.get(['id'])!.value,
      nomS: this.editForm.get(['nomS'])!.value,
      lieu: this.editForm.get(['lieu'])!.value,
      match: this.editForm.get(['match'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStade>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IMatch): any {
    return item.id;
  }
}
