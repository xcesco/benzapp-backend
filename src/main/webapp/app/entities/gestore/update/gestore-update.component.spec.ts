jest.mock('@angular/router');

import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GestoreService } from '../service/gestore.service';
import { Gestore } from '../gestore.model';
import { Fascia } from 'app/entities/fascia/fascia.model';
import { Marchio } from 'app/entities/marchio/marchio.model';

import { GestoreUpdateComponent } from './gestore-update.component';

describe('Component Tests', () => {
  describe('Gestore Management Update Component', () => {
    let comp: GestoreUpdateComponent;
    let fixture: ComponentFixture<GestoreUpdateComponent>;
    let service: GestoreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GestoreUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GestoreUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GestoreUpdateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(GestoreService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Gestore(123);
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
        const entity = new Gestore();
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

      describe('trackMarchioById', () => {
        it('Should return tracked Marchio primary key', () => {
          const entity = new Marchio(123);
          const trackResult = comp.trackMarchioById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
