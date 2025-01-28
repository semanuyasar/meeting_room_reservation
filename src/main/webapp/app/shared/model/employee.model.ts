export interface IEmployee {
  id?: number;
  department?: string | null;
}

export const defaultValue: Readonly<IEmployee> = {};
