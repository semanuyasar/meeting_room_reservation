import dayjs from 'dayjs';
import { IMeetingRoom } from 'app/shared/model/meeting-room.model';
import { IUser } from 'app/shared/model/user.model';

export interface IReservation {
  id?: number;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  duration?: string | null;
  room?: IMeetingRoom | null;
  employees?: IMeetingRoom[] | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<IReservation> = {};
