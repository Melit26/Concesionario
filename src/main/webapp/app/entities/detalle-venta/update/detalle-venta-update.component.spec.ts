import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DetalleVentaService } from '../service/detalle-venta.service';
import { IDetalleVenta, DetalleVenta } from '../detalle-venta.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';

import { DetalleVentaUpdateComponent } from './detalle-venta-update.component';

describe('DetalleVenta Management Update Component', () => {
  let comp: DetalleVentaUpdateComponent;
  let fixture: ComponentFixture<DetalleVentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detalleVentaService: DetalleVentaService;
  let ventaService: VentaService;
  let cocheService: CocheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DetalleVentaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DetalleVentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetalleVentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detalleVentaService = TestBed.inject(DetalleVentaService);
    ventaService = TestBed.inject(VentaService);
    cocheService = TestBed.inject(CocheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Venta query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const venta: IVenta = { id: 78957 };
      detalleVenta.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 12675 }];
      jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
      expect(comp.ventasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call coche query and add missing value', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const coche: ICoche = { id: 63296 };
      detalleVenta.coche = coche;

      const cocheCollection: ICoche[] = [{ id: 86785 }];
      jest.spyOn(cocheService, 'query').mockReturnValue(of(new HttpResponse({ body: cocheCollection })));
      const expectedCollection: ICoche[] = [coche, ...cocheCollection];
      jest.spyOn(cocheService, 'addCocheToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(cocheService.query).toHaveBeenCalled();
      expect(cocheService.addCocheToCollectionIfMissing).toHaveBeenCalledWith(cocheCollection, coche);
      expect(comp.cochesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const detalleVenta: IDetalleVenta = { id: 456 };
      const venta: IVenta = { id: 84589 };
      detalleVenta.venta = venta;
      const coche: ICoche = { id: 32871 };
      detalleVenta.coche = coche;

      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(detalleVenta));
      expect(comp.ventasSharedCollection).toContain(venta);
      expect(comp.cochesCollection).toContain(coche);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleVenta>>();
      const detalleVenta = { id: 123 };
      jest.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleVenta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(detalleVentaService.update).toHaveBeenCalledWith(detalleVenta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleVenta>>();
      const detalleVenta = new DetalleVenta();
      jest.spyOn(detalleVentaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detalleVenta }));
      saveSubject.complete();

      // THEN
      expect(detalleVentaService.create).toHaveBeenCalledWith(detalleVenta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DetalleVenta>>();
      const detalleVenta = { id: 123 };
      jest.spyOn(detalleVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detalleVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detalleVentaService.update).toHaveBeenCalledWith(detalleVenta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVentaById', () => {
      it('Should return tracked Venta primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVentaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCocheById', () => {
      it('Should return tracked Coche primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCocheById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
