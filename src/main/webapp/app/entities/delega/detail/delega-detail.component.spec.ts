import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Delega } from '../delega.model';

import { DelegaDetailComponent } from './delega-detail.component';

describe('Component Tests', () => {
  describe('Delega Management Detail Component', () => {
    let comp: DelegaDetailComponent;
    let fixture: ComponentFixture<DelegaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DelegaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ delega: new Delega(123) }) },
          },
        ],
      })
        .overrideTemplate(DelegaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DelegaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load delega on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.delega).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
