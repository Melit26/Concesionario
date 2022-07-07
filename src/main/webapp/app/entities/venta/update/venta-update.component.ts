import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVenta, Venta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IVendedor } from 'app/entities/vendedor/vendedor.model';
import { VendedorService } from 'app/entities/vendedor/service/vendedor.service';


@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;

  clientesSharedCollection: ICliente[] = [];
  vendedoresSharedCollection: IVendedor[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [null, [Validators.required]],
    metodoPago: [null, [Validators.required]],
    cliente: [null, Validators.required],
    vendedor: [null, Validators.required],

  });

  constructor(
    protected ventaService: VentaService,
    protected clienteService: ClienteService,
    protected vendedorService: VendedorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.updateForm(venta);

      this.loadRelationshipsOptions();
      this.loadRelationshipsOptionsVendedores();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.createFromForm();
    if (venta.id !== undefined) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  trackClienteById(_index: number, item: ICliente): number {
    return item.id!;
  }

  trackVendedorById(_index: number, item: IVendedor): number {
    return item.id!;
    
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
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

  protected updateForm(venta: IVenta): void {
    this.editForm.patchValue({
      id: venta.id,
      fecha: venta.fecha,
      metodoPago: venta.metodoPago,
      cliente: venta.cliente,
      vendedor: venta.vendedor
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, venta.cliente);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
  }
  protected loadRelationshipsOptionsVendedores(): void {
    this.vendedorService
      .query()
      .pipe(map((res: HttpResponse<IVendedor[]>) => res.body ?? []))
      .pipe(
        map((vendedores: IVendedor[]) => this.vendedorService.addVendedorToCollectionIfMissing(vendedores, this.editForm.get('vendedor')!.value))
      )
      .subscribe((vendedores: IVendedor[]) => (this.vendedoresSharedCollection = vendedores));
  }

  protected createFromForm(): IVenta {
    return {
      ...new Venta(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      metodoPago: this.editForm.get(['metodoPago'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      vendedor: this.editForm.get(['vendedor'])!.value
    };
  }
}
