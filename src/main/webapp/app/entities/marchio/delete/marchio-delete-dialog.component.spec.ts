jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MarchioService } from '../service/marchio.service';

import { MarchioDeleteDialogComponent } from './marchio-delete-dialog.component';

describe('Component Tests', () => {
  describe('Marchio Management Delete Component', () => {
    let comp: MarchioDeleteDialogComponent;
    let fixture: ComponentFixture<MarchioDeleteDialogComponent>;
    let service: MarchioService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MarchioDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(MarchioDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MarchioDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MarchioService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
