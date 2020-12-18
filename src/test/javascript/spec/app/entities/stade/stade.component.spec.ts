import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MywebTestModule } from '../../../test.module';
import { StadeComponent } from 'app/entities/stade/stade.component';
import { StadeService } from 'app/entities/stade/stade.service';
import { Stade } from 'app/shared/model/stade.model';

describe('Component Tests', () => {
  describe('Stade Management Component', () => {
    let comp: StadeComponent;
    let fixture: ComponentFixture<StadeComponent>;
    let service: StadeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MywebTestModule],
        declarations: [StadeComponent],
      })
        .overrideTemplate(StadeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StadeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StadeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Stade(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.stades && comp.stades[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
