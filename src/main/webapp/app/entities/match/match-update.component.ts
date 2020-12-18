import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMatch, Match } from 'app/shared/model/match.model';
import { MatchService } from './match.service';

@Component({
  selector: 'jhi-match-update',
  templateUrl: './match-update.component.html',
})
export class MatchUpdateComponent implements OnInit {
  isSaving = false;
  dateMDp: any;

  editForm = this.fb.group({
    id: [],
    nomM: [],
    numM: [],
    dureeM: [],
    dateM: [],
  });

  constructor(protected matchService: MatchService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ match }) => {
      this.updateForm(match);
    });
  }

  updateForm(match: IMatch): void {
    this.editForm.patchValue({
      id: match.id,
      nomM: match.nomM,
      numM: match.numM,
      dureeM: match.dureeM,
      dateM: match.dateM,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const match = this.createFromForm();
    if (match.id !== undefined) {
      this.subscribeToSaveResponse(this.matchService.update(match));
    } else {
      this.subscribeToSaveResponse(this.matchService.create(match));
    }
  }

  private createFromForm(): IMatch {
    return {
      ...new Match(),
      id: this.editForm.get(['id'])!.value,
      nomM: this.editForm.get(['nomM'])!.value,
      numM: this.editForm.get(['numM'])!.value,
      dureeM: this.editForm.get(['dureeM'])!.value,
      dateM: this.editForm.get(['dateM'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatch>>): void {
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
}
