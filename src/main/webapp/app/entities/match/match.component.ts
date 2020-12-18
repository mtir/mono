import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatch } from 'app/shared/model/match.model';
import { MatchService } from './match.service';
import { MatchDeleteDialogComponent } from './match-delete-dialog.component';

@Component({
  selector: 'jhi-match',
  templateUrl: './match.component.html',
})
export class MatchComponent implements OnInit, OnDestroy {
  matches?: IMatch[];
  eventSubscriber?: Subscription;

  constructor(protected matchService: MatchService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.matchService.query().subscribe((res: HttpResponse<IMatch[]>) => (this.matches = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMatches();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMatch): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMatches(): void {
    this.eventSubscriber = this.eventManager.subscribe('matchListModification', () => this.loadAll());
  }

  delete(match: IMatch): void {
    const modalRef = this.modalService.open(MatchDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.match = match;
  }
}
