jest.mock('@angular/router');

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TesseraService } from '../service/tessera.service';
import { Tessera } from '../tessera.model';
import { Fascia } from 'app/entities/fascia/fascia.model';
import { Cittadino } from 'app/entities/cittadino/cittadino.model';

import { TesseraUpdateComponent } from './tessera-update.component';

describe('Component Tests', () => {
  describe('Tessera Management Update Component', () => {
    let comp: TesseraUpdateComponent;
    let fixture: ComponentFixture<TesseraUpdateComponent>;
    let service: TesseraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TesseraUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TesseraUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TesseraUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TesseraService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Tessera(123);
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
        const entity = new Tessera();
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

      describe('trackCittadinoById', () => {
        it('Should return tracked Cittadino primary key', () => {
          const entity = new Cittadino(123);
          const trackResult = comp.trackCittadinoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
