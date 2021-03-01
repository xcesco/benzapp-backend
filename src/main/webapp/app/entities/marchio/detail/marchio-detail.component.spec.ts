import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Marchio } from '../marchio.model';
import { DataUtils } from 'app/core/util/data-util.service';

import { MarchioDetailComponent } from './marchio-detail.component';

describe('Component Tests', () => {
  describe('Marchio Management Detail Component', () => {
    let comp: MarchioDetailComponent;
    let fixture: ComponentFixture<MarchioDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MarchioDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ marchio: new Marchio(123) }) },
          },
        ],
      })
        .overrideTemplate(MarchioDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MarchioDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load marchio on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.marchio).toEqual(jasmine.objectContaining({ id: 123 }));
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
