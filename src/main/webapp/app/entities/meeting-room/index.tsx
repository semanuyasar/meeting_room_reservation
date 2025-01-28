import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MeetingRoom from './meeting-room';
import MeetingRoomDetail from './meeting-room-detail';
import MeetingRoomUpdate from './meeting-room-update';
import MeetingRoomDeleteDialog from './meeting-room-delete-dialog';

const MeetingRoomRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MeetingRoom />} />
    <Route path="new" element={<MeetingRoomUpdate />} />
    <Route path=":id">
      <Route index element={<MeetingRoomDetail />} />
      <Route path="edit" element={<MeetingRoomUpdate />} />
      <Route path="delete" element={<MeetingRoomDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MeetingRoomRoutes;
