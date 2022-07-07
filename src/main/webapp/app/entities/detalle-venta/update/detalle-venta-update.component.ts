import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDetalleVenta, DetalleVenta } from '../detalle-venta.model';
import { DetalleVentaService } from '../service/detalle-venta.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';

@Component({
  selector: 'jhi-detalle-venta-update',
  templateUrl: './detalle-venta-update.component.html',
})
export class DetalleVentaUpdateComponent implements OnInit {
  isSaving = false;

  ventasSharedCollection: IVenta[] = [];
  cochesCollection: ICoche[] = [];

  editForm = this.fb.group({
    id: [],
    descuento: [null, [Validators.required]],
    venta: [null, Validators.required],
    coche: [],
  });

  constructor(
    protected detalleVentaService: DetalleVentaService,
    protected ventaService: VentaService,
    protected cocheService: CocheService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detalleVenta }) => {
      this.updateForm(detalleVenta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detalleVenta = this.createFromForm();
    if (detalleVenta.id !== undefined) {
      this.subscribeToSaveResponse(this.detalleVentaService.update(detalleVenta));
    } else {
      this.subscribeToSaveResponse(this.detalleVentaService.create(detalleVenta));
    }
  }

  trackVentaById(_index: number, item: IVenta): number {
    return item.id!;
  }

  trackCocheById(_index: number, item: ICoche): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetalleVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(detalleVenta: IDetalleVenta): void {
    this.editForm.patchValue({
      id: detalleVenta.id,
      descuento: detalleVenta.descuento,
      venta: detalleVenta.venta,
      coche: detalleVenta.coche,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, detalleVenta.venta);
    this.cochesCollection = this.cocheService.addCocheToCollectionIfMissing(this.cochesCollection, detalleVenta.coche);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));

    this.cocheService
      .query({ filter: 'detalleventa-is-null' })
      .pipe(map((res: HttpResponse<ICoche[]>) => res.body ?? []))
      .pipe(map((coches: ICoche[]) => this.cocheService.addCocheToCollectionIfMissing(coches, this.editForm.get('coche')!.value)))
      .subscribe((coches: ICoche[]) => (this.cochesCollection = coches));
  }

  protected createFromForm(): IDetalleVenta {
    return {
      ...new DetalleVenta(),
      id: this.editForm.get(['id'])!.value,
      descuento: this.editForm.get(['descuento'])!.value,
      venta: this.editForm.get(['venta'])!.value,
      coche: this.editForm.get(['coche'])!.value,
    };
  }
}
