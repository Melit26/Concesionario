import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    primerApellido: [null, [Validators.required]],
    segundoApellido: [null, [Validators.required]],
    tipoIdentificacion: [null, [Validators.required]],
    numIdentificacion: [null, [Validators.required, Validators.minLength(9)]],
    telefono: [null, [Validators.required, Validators.minLength(9)]],
    mail: [null, [Validators.required]],
    direccion: [null, [Validators.required]],
    ocupacion: [null, [Validators.required]],
  });

  constructor(protected clienteService: ClienteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.updateForm(cliente);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
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

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      nombre: cliente.nombre,
      primerApellido: cliente.primerApellido,
      segundoApellido: cliente.segundoApellido,
      tipoIdentificacion: cliente.tipoIdentificacion,
      numIdentificacion: cliente.numIdentificacion,
      telefono: cliente.telefono,
      mail: cliente.mail,
      direccion: cliente.direccion,
      ocupacion: cliente.ocupacion,
    });
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      primerApellido: this.editForm.get(['primerApellido'])!.value,
      segundoApellido: this.editForm.get(['segundoApellido'])!.value,
      tipoIdentificacion: this.editForm.get(['tipoIdentificacion'])!.value,
      numIdentificacion: this.editForm.get(['numIdentificacion'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      ocupacion: this.editForm.get(['ocupacion'])!.value,
    };
  }
}
