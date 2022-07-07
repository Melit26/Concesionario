import { IVenta } from 'app/entities/venta/venta.model';
import { ICoche } from 'app/entities/coche/coche.model';

export interface IDetalleVenta {
  id?: number;
  descuento?: string;
  venta?: IVenta;
  coche?: ICoche | null;
}

export class DetalleVenta implements IDetalleVenta {
  constructor(
    public id?: number,
    public descuento?: string,
    public venta?: IVenta,
    public coche?: ICoche | null
  ) {}
}

export function getDetalleVentaIdentifier(detalleVenta: IDetalleVenta): number | undefined {
  return detalleVenta.id;
}
