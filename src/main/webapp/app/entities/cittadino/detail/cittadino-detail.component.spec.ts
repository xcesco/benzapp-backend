import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Cittadino } from '../cittadino.model';

import { CittadinoDetailComponent } from './cittadino-detail.component';

describe('Component Tests', () => {
  describe('Cittadino Management Detail Component', () => {
    let comp: CittadinoDetailComponent;
    let fixture: ComponentFixture<CittadinoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CittadinoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ cittadino: new Cittadino(123) }) },
          },
        ],
      })
        .overrideTemplate(CittadinoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CittadinoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load cittadino on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.cittadino).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
