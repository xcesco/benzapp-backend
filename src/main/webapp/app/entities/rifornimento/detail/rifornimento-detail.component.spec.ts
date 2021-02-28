import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Rifornimento } from '../rifornimento.model';

import { RifornimentoDetailComponent } from './rifornimento-detail.component';

describe('Component Tests', () => {
  describe('Rifornimento Management Detail Component', () => {
    let comp: RifornimentoDetailComponent;
    let fixture: ComponentFixture<RifornimentoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RifornimentoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ rifornimento: new Rifornimento(123) }) },
          },
        ],
      })
        .overrideTemplate(RifornimentoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RifornimentoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load rifornimento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rifornimento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
