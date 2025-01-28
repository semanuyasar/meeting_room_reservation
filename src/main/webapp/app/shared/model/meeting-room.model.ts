import { IReservation } from 'app/shared/model/reservation.model';

export interface IMeetingRoom {
  id?: number;
  name?: string | null;
  location?: string | null;
  capacity?: number | null;
  reservations?: IReservation[] | null;
}

export const defaultValue: Readonly<IMeetingRoom> = {};
