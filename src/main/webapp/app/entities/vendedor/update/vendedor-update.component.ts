import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVendedor, Vendedor } from '../vendedor.model';
import { VendedorService } from '../service/vendedor.service';

@Component({
  selector: 'jhi-vendedor-update',
  templateUrl: './vendedor-update.component.html',
})
export class VendedorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    primerApellido: [null, [Validators.required]],
    segundoApellido: [null, [Validators.required]],
    mail: [null, [Validators.required]],
    tipoIdentificacion: [null, [Validators.required]],
    numIdentificacion: [null, [Validators.required]],
    direccion: [null, [Validators.required]],
    cargo: [null, [Validators.required]],
  });

  constructor(protected vendedorService: VendedorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendedor }) => {
      this.updateForm(vendedor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendedor = this.createFromForm();
    if (vendedor.id !== undefined) {
      this.subscribeToSaveResponse(this.vendedorService.update(vendedor));
    } else {
      this.subscribeToSaveResponse(this.vendedorService.create(vendedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendedor>>): void {
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

  protected updateForm(vendedor: IVendedor): void {
    this.editForm.patchValue({
      id: vendedor.id,
      nombre: vendedor.nombre,
      primerApellido: vendedor.primerApellido,
      segundoApellido: vendedor.segundoApellido,
      mail: vendedor.mail,
      tipoIdentificacion: vendedor.tipoIdentificacion,
      numIdentificacion: vendedor.numIdentificacion,
      direccion: vendedor.direccion,
      cargo: vendedor.cargo,
    });
  }

  protected createFromForm(): IVendedor {
    return {
      ...new Vendedor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      primerApellido: this.editForm.get(['primerApellido'])!.value,
      segundoApellido: this.editForm.get(['segundoApellido'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      tipoIdentificacion: this.editForm.get(['tipoIdentificacion'])!.value,
      numIdentificacion: this.editForm.get(['numIdentificacion'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      cargo: this.editForm.get(['cargo'])!.value,
    };
  }
}
