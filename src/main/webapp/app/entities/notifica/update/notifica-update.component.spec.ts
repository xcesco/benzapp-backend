jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NotificaService } from '../service/notifica.service';
import { INotifica, Notifica } from '../notifica.model';

import { NotificaUpdateComponent } from './notifica-update.component';

describe('Component Tests', () => {
  describe('Notifica Management Update Component', () => {
    let comp: NotificaUpdateComponent;
    let fixture: ComponentFixture<NotificaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let notificaService: NotificaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NotificaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NotificaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NotificaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      notificaService = TestBed.inject(NotificaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const notifica: INotifica = { id: 456 };

        activatedRoute.data = of({ notifica });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(notifica));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notifica = { id: 123 };
        spyOn(notificaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notifica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notifica }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(notificaService.update).toHaveBeenCalledWith(notifica);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notifica = new Notifica();
        spyOn(notificaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notifica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: notifica }));
        saveSubject.complete();

        // THEN
        expect(notificaService.create).toHaveBeenCalledWith(notifica);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const notifica = { id: 123 };
        spyOn(notificaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ notifica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(notificaService.update).toHaveBeenCalledWith(notifica);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
