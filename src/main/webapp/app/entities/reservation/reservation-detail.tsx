import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reservation.reducer';

export const ReservationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservationEntity = useAppSelector(state => state.reservation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservationDetailsHeading">
          <Translate contentKey="meetingroomreservationApp.reservation.detail.title">Reservation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="meetingroomreservationApp.reservation.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.startTime ? (
              <TextFormat value={reservationEntity.startTime} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="meetingroomreservationApp.reservation.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.endTime ? <TextFormat value={reservationEntity.endTime} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="duration">
              <Translate contentKey="meetingroomreservationApp.reservation.duration">Duration</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.duration ? <DurationFormat value={reservationEntity.duration} /> : null} ({reservationEntity.duration})
          </dd>
          <dt>
            <Translate contentKey="meetingroomreservationApp.reservation.room">Room</Translate>
          </dt>
          <dd>{reservationEntity.room ? reservationEntity.room.id : ''}</dd>
          <dt>
            <Translate contentKey="meetingroomreservationApp.reservation.employee">Employee</Translate>
          </dt>
          <dd>
            {reservationEntity.employees
              ? reservationEntity.employees.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {reservationEntity.employees && i === reservationEntity.employees.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="meetingroomreservationApp.reservation.owner">Owner</Translate>
          </dt>
          <dd>{reservationEntity.owner ? reservationEntity.owner.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reservation/${reservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservationDetail;
