import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IVendedor } from '../vendedor/vendedor.model';

export interface IVenta {
  id?: number;
  fecha?: dayjs.Dayjs;
  metodoPago?: string;
  cliente?: ICliente;
  vendedor?: IVendedor;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs,
    public metodoPago?: string,
    public cliente?: ICliente,
    public vendedor?: IVendedor
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}
