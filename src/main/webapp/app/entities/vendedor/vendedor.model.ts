export interface IVendedor {
  id?: number;
  nombre?: string;
  primerApellido?: string;
  segundoApellido?: string;
  mail?: string;
  tipoIdentificacion?: string;
  numIdentificacion?: string;
  direccion?: string;
  cargo?: string;
}

export class Vendedor implements IVendedor {
  constructor(
    public id?: number,
    public nombre?: string,
    public primerApellido?: string,
    public segundoApellido?: string,
    public mail?: string,
    public tipoIdentificacion?: string,
    public numIdentificacion?: string,
    public direccion?: string,
    public cargo?: string
  ) {}
}

export function getVendedorIdentifier(vendedor: IVendedor): number | undefined {
  return vendedor.id;
}
