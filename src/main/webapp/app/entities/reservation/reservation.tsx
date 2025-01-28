import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './reservation.reducer';

interface Reservation {
  id: number;
  startTime: string;
  endTime: string;
  room: { id: number };
  employees: { id: number }[];
}

export const Reservation: React.FC = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(getSortState(pageLocation, 'id'));
  const reservationList = useAppSelector(state => state.reservation.entities);
  const loading = useAppSelector(state => state.reservation.loading);

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

  const sort = (p: string) => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string): any => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  const calculateDuration = (start: string, end: string): number => {
    const startDate = new Date(start);
    const endDate = new Date(end);

    if (startDate > endDate) return 0;

    const diffMs = endDate.getTime() - startDate.getTime();
    return Math.floor(diffMs / 60000); // Convert milliseconds to minutes
  };

  return (
    <div>
      <h2 id="reservation-heading" data-cy="ReservationHeading">
        <Translate contentKey="meetingroomreservationApp.reservation.home.title">Reservations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="meetingroomreservationApp.reservation.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/reservation/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="meetingroomreservationApp.reservation.home.createLabel">Create new Reservation</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {reservationList && reservationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="meetingroomreservationApp.reservation.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  <Translate contentKey="meetingroomreservationApp.reservation.startTime">Start Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startTime')} />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="meetingroomreservationApp.reservation.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endTime')} />
                </th>
                <th>
                  <Translate contentKey="meetingroomreservationApp.reservation.duration">Duration (minutes)</Translate>
                </th>
                <th>
                  <Translate contentKey="meetingroomreservationApp.reservation.room">Room</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="meetingroomreservationApp.reservation.employee">Employee</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {reservationList.map((reservation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/reservation/${reservation.id}`} color="link" size="sm">
                      {reservation.id}
                    </Button>
                  </td>
                  <td>
                    {reservation.startTime ? <TextFormat type="date" value={reservation.startTime} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {reservation.endTime ? <TextFormat type="date" value={reservation.endTime} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{calculateDuration(reservation.startTime, reservation.endTime)}</td>
                  <td>{reservation.room ? <Link to={`/meeting-room/${reservation.room.id}`}>{reservation.room.id}</Link> : ''}</td>
                  <td>
                    {reservation.employees
                      ? reservation.employees.map((val, j) => (
                          <span key={j}>
                            <Link to={`/meeting-room/${val.id}`}>{val.id}</Link>
                            {j === reservation.employees.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>{reservation.owner ? reservation.owner.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/reservation/${reservation.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/reservation/${reservation.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/reservation/${reservation.id}/delete`)}
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
              <Translate contentKey="meetingroomreservationApp.reservation.home.notFound">No Reservations found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Reservation;
