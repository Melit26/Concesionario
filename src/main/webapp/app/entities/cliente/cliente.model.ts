export interface ICliente {
  id?: number;
  nombre?: string;
  primerApellido?: string;
  segundoApellido?: string;
  tipoIdentificacion?: string;
  numIdentificacion?: string;
  telefono?: string;
  mail?: string;
  direccion?: string;
  ocupacion?: string;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string,
    public primerApellido?: string,
    public segundoApellido?: string,
    public tipoIdentificacion?: string,
    public numIdentificacion?: string,
    public telefono?: string,
    public mail?: string,
    public direccion?: string,
    public ocupacion?: string
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
