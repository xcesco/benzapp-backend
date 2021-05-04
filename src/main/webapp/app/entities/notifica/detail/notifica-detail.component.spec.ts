import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NotificaDetailComponent } from './notifica-detail.component';

describe('Component Tests', () => {
  describe('Notifica Management Detail Component', () => {
    let comp: NotificaDetailComponent;
    let fixture: ComponentFixture<NotificaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NotificaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ notifica: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NotificaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NotificaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load notifica on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.notifica).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
