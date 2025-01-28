import employee from 'app/entities/employee/employee.reducer';
import meetingRoom from 'app/entities/meeting-room/meeting-room.reducer';
import reservation from 'app/entities/reservation/reservation.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  employee,
  meetingRoom,
  reservation,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
