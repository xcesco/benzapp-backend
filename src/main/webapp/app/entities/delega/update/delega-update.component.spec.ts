jest.mock('@angular/router');

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DelegaService } from '../service/delega.service';
import { Delega } from '../delega.model';
import { Cittadino } from 'app/entities/cittadino/cittadino.model';
import { Tessera } from 'app/entities/tessera/tessera.model';

import { DelegaUpdateComponent } from './delega-update.component';

describe('Component Tests', () => {
  describe('Delega Management Update Component', () => {
    let comp: DelegaUpdateComponent;
    let fixture: ComponentFixture<DelegaUpdateComponent>;
    let service: DelegaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DelegaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DelegaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DelegaUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DelegaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Delega(123);
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
        const entity = new Delega();
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
      describe('trackCittadinoById', () => {
        it('Should return tracked Cittadino primary key', () => {
          const entity = new Cittadino(123);
          const trackResult = comp.trackCittadinoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTesseraById', () => {
        it('Should return tracked Tessera primary key', () => {
          const entity = new Tessera(123);
          const trackResult = comp.trackTesseraById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
