import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './meeting-room.reducer';

export const MeetingRoomDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const meetingRoomEntity = useAppSelector(state => state.meetingRoom.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="meetingRoomDetailsHeading">
          <Translate contentKey="meetingroomreservationApp.meetingRoom.detail.title">MeetingRoom</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{meetingRoomEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="meetingroomreservationApp.meetingRoom.name">Name</Translate>
            </span>
          </dt>
          <dd>{meetingRoomEntity.name}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="meetingroomreservationApp.meetingRoom.location">Location</Translate>
            </span>
          </dt>
          <dd>{meetingRoomEntity.location}</dd>
          <dt>
            <span id="capacity">
              <Translate contentKey="meetingroomreservationApp.meetingRoom.capacity">Capacity</Translate>
            </span>
          </dt>
          <dd>{meetingRoomEntity.capacity}</dd>
          <dt>
            <Translate contentKey="meetingroomreservationApp.meetingRoom.reservation">Reservation</Translate>
          </dt>
          <dd>
            {meetingRoomEntity.reservations
              ? meetingRoomEntity.reservations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {meetingRoomEntity.reservations && i === meetingRoomEntity.reservations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/meeting-room" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/meeting-room/${meetingRoomEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MeetingRoomDetail;
