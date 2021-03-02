import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FasciaService } from '../service/fascia.service';
import { Fascia } from '../fascia.model';

import { FasciaComponent } from './fascia.component';

describe('Component Tests', () => {
  describe('Fascia Management Component', () => {
    let comp: FasciaComponent;
    let fixture: ComponentFixture<FasciaComponent>;
    let service: FasciaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FasciaComponent],
      })
        .overrideTemplate(FasciaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FasciaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FasciaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Fascia(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fascias?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
