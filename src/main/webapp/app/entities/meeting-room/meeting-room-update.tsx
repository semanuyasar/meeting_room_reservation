import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getReservations } from 'app/entities/reservation/reservation.reducer';
import { createEntity, getEntity, reset, updateEntity } from './meeting-room.reducer';

export const MeetingRoomUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const reservations = useAppSelector(state => state.reservation.entities);
  const meetingRoomEntity = useAppSelector(state => state.meetingRoom.entity);
  const loading = useAppSelector(state => state.meetingRoom.loading);
  const updating = useAppSelector(state => state.meetingRoom.updating);
  const updateSuccess = useAppSelector(state => state.meetingRoom.updateSuccess);

  const handleClose = () => {
    navigate('/meeting-room');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getReservations({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.capacity !== undefined && typeof values.capacity !== 'number') {
      values.capacity = Number(values.capacity);
    }

    const entity = {
      ...meetingRoomEntity,
      ...values,
      reservations: mapIdList(values.reservations),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...meetingRoomEntity,
          reservations: meetingRoomEntity?.reservations?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="meetingroomreservationApp.meetingRoom.home.createOrEditLabel" data-cy="MeetingRoomCreateUpdateHeading">
            <Translate contentKey="meetingroomreservationApp.meetingRoom.home.createOrEditLabel">Create or edit a MeetingRoom</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="meeting-room-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('meetingroomreservationApp.meetingRoom.name')}
                id="meeting-room-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('meetingroomreservationApp.meetingRoom.location')}
                id="meeting-room-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('meetingroomreservationApp.meetingRoom.capacity')}
                id="meeting-room-capacity"
                name="capacity"
                data-cy="capacity"
                type="text"
              />
              <ValidatedField
                label={translate('meetingroomreservationApp.meetingRoom.reservation')}
                id="meeting-room-reservation"
                data-cy="reservation"
                type="select"
                multiple
                name="reservations"
              >
                <option value="" key="0" />
                {reservations
                  ? reservations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/meeting-room" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MeetingRoomUpdate;
