import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStade } from 'app/shared/model/stade.model';
import { StadeService } from './stade.service';
import { StadeDeleteDialogComponent } from './stade-delete-dialog.component';

@Component({
  selector: 'jhi-stade',
  templateUrl: './stade.component.html',
})
export class StadeComponent implements OnInit, OnDestroy {
  stades?: IStade[];
  eventSubscriber?: Subscription;

  constructor(protected stadeService: StadeService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.stadeService.query().subscribe((res: HttpResponse<IStade[]>) => (this.stades = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStades();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStade): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStades(): void {
    this.eventSubscriber = this.eventManager.subscribe('stadeListModification', () => this.loadAll());
  }

  delete(stade: IStade): void {
    const modalRef = this.modalService.open(StadeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.stade = stade;
  }
}
