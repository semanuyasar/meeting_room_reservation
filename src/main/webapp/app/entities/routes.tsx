import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Employee from './employee';
import MeetingRoom from './meeting-room';
import Reservation from './reservation';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="employee/*" element={<Employee />} />
        <Route path="meeting-room/*" element={<MeetingRoom />} />
        <Route path="reservation/*" element={<Reservation />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
