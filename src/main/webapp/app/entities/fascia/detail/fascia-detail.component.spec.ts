import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Fascia } from '../fascia.model';

import { FasciaDetailComponent } from './fascia-detail.component';

describe('Component Tests', () => {
  describe('Fascia Management Detail Component', () => {
    let comp: FasciaDetailComponent;
    let fixture: ComponentFixture<FasciaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FasciaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fascia: new Fascia(123) }) },
          },
        ],
      })
        .overrideTemplate(FasciaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FasciaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fascia on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fascia).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
