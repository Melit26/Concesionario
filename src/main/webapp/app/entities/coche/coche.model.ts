export interface ICoche {
  id?: number;
  marca?: string;
  modelo?: string;
  kilometraje?: number;
  color?: string;
  tipoCoche?: string;
  estado?: string;
  precio?: number;
}

export class Coche implements ICoche {
  constructor(
    public id?: number,
    public marca?: string,
    public modelo?: string,
    public kilometraje?: number,
    public color?: string,
    public tipoCoche?: string,
    public estado?: string,
    public precio?: number
  ) {}
}

export function getCocheIdentifier(coche: ICoche): number | undefined {
  return coche.id;
}
