jest.mock('@angular/router');

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CittadinoService } from '../service/cittadino.service';
import { Cittadino } from '../cittadino.model';
import { Fascia } from 'app/entities/fascia/fascia.model';

import { CittadinoUpdateComponent } from './cittadino-update.component';

describe('Component Tests', () => {
  describe('Cittadino Management Update Component', () => {
    let comp: CittadinoUpdateComponent;
    let fixture: ComponentFixture<CittadinoUpdateComponent>;
    let service: CittadinoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CittadinoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CittadinoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CittadinoUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CittadinoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Cittadino(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Cittadino();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFasciaById', () => {
        it('Should return tracked Fascia primary key', () => {
          const entity = new Fascia(123);
          const trackResult = comp.trackFasciaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
