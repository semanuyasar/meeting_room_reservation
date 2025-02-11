import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './meeting-room.reducer';

export const MeetingRoom = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const meetingRoomList = useAppSelector(state => state.meetingRoom.entities);
  const loading = useAppSelector(state => state.meetingRoom.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="meeting-room-heading" data-cy="MeetingRoomHeading">
        <Translate contentKey="meetingroomreservationApp.meetingRoom.home.title">Meeting Rooms</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="meetingroomreservationApp.meetingRoom.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/meeting-room/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="meetingroomreservationApp.meetingRoom.home.createLabel">Create new Meeting Room</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {meetingRoomList && meetingRoomList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="meetingroomreservationApp.meetingRoom.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="meetingroomreservationApp.meetingRoom.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('location')}>
                  <Translate contentKey="meetingroomreservationApp.meetingRoom.location">Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('location')} />
                </th>
                <th className="hand" onClick={sort('capacity')}>
                  <Translate contentKey="meetingroomreservationApp.meetingRoom.capacity">Capacity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('capacity')} />
                </th>
                <th>
                  <Translate contentKey="meetingroomreservationApp.meetingRoom.reservation">Reservation</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {meetingRoomList.map((meetingRoom, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/meeting-room/${meetingRoom.id}`} color="link" size="sm">
                      {meetingRoom.id}
                    </Button>
                  </td>
                  <td>{meetingRoom.name}</td>
                  <td>{meetingRoom.location}</td>
                  <td>{meetingRoom.capacity}</td>
                  <td>
                    {meetingRoom.reservations
                      ? meetingRoom.reservations.map((val, j) => (
                          <span key={j}>
                            <Link to={`/reservation/${val.id}`}>{val.id}</Link>
                            {j === meetingRoom.reservations.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/meeting-room/${meetingRoom.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/meeting-room/${meetingRoom.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/meeting-room/${meetingRoom.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="meetingroomreservationApp.meetingRoom.home.notFound">No Meeting Rooms found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MeetingRoom;
