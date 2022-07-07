import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICoche, Coche } from '../coche.model';
import { CocheService } from '../service/coche.service';

@Component({
  selector: 'jhi-coche-update',
  templateUrl: './coche-update.component.html',
})
export class CocheUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    marca: [null, [Validators.required]],
    modelo: [null, [Validators.required]],
    kilometraje: [null, [Validators.required]],
    color: [null, [Validators.required]],
    tipoCoche: [null, [Validators.required]],
    estado: [null, [Validators.required]],
    precio: [null, [Validators.required]],
  });

  constructor(protected cocheService: CocheService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coche }) => {
      this.updateForm(coche);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coche = this.createFromForm();
    if (coche.id !== undefined) {
      this.subscribeToSaveResponse(this.cocheService.update(coche));
    } else {
      this.subscribeToSaveResponse(this.cocheService.create(coche));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoche>>): void {
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

  protected updateForm(coche: ICoche): void {
    this.editForm.patchValue({
      id: coche.id,
      marca: coche.marca,
      modelo: coche.modelo,
      kilometraje: coche.kilometraje,
      color: coche.color,
      tipoCoche: coche.tipoCoche,
      estado: coche.estado,
      precio: coche.precio,
    });
  }

  protected createFromForm(): ICoche {
    return {
      ...new Coche(),
      id: this.editForm.get(['id'])!.value,
      marca: this.editForm.get(['marca'])!.value,
      modelo: this.editForm.get(['modelo'])!.value,
      kilometraje: this.editForm.get(['kilometraje'])!.value,
      color: this.editForm.get(['color'])!.value,
      tipoCoche: this.editForm.get(['tipoCoche'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      precio: this.editForm.get(['precio'])!.value,
    };
  }
}
