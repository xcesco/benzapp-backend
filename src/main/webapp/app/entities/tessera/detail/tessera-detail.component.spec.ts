import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Tessera } from '../tessera.model';
import { DataUtils } from 'app/core/util/data-util.service';

import { TesseraDetailComponent } from './tessera-detail.component';

describe('Component Tests', () => {
  describe('Tessera Management Detail Component', () => {
    let comp: TesseraDetailComponent;
    let fixture: ComponentFixture<TesseraDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TesseraDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tessera: new Tessera(123) }) },
          },
        ],
      })
        .overrideTemplate(TesseraDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TesseraDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load tessera on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tessera).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
