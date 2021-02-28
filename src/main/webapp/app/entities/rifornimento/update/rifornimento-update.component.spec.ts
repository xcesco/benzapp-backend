jest.mock('@angular/router');

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RifornimentoService } from '../service/rifornimento.service';
import { Rifornimento } from '../rifornimento.model';
import { Gestore } from 'app/entities/gestore/gestore.model';
import { Tessera } from 'app/entities/tessera/tessera.model';

import { RifornimentoUpdateComponent } from './rifornimento-update.component';

describe('Component Tests', () => {
  describe('Rifornimento Management Update Component', () => {
    let comp: RifornimentoUpdateComponent;
    let fixture: ComponentFixture<RifornimentoUpdateComponent>;
    let service: RifornimentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RifornimentoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RifornimentoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RifornimentoUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(RifornimentoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Rifornimento(123);
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
        const entity = new Rifornimento();
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
      describe('trackGestoreById', () => {
        it('Should return tracked Gestore primary key', () => {
          const entity = new Gestore(123);
          const trackResult = comp.trackGestoreById(0, entity);
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
