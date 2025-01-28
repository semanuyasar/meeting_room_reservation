import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row className="home-container">
      <Col md="12">
        <h1 className="display-4 home-header">
          <Translate contentKey="home.title">Hello, Java Hipster!</Translate>
        </h1>
        {account?.login ? (
          <Link to="/reservation/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="meetingroomreservationApp.reservation.home.createLabel">Create new Reservation</Translate>
          </Link>
        ) : (
          <>
            <Col md="4">
              <div className="sign-in-button-container">
                <p>
                  <Translate contentKey="global.messages.info.authenticated.prefix">If you want to</Translate>{' '}
                  <Link to="/login" className="btn">
                    <Translate contentKey="global.messages.info.authenticated.link">sign in</Translate>
                  </Link>
                  <Translate contentKey="global.messages.info.authenticated.suffix">
                    , you can try the default accounts:
                    <br /> - Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                    <br /> - User (login=&quot;user&quot; and password=&quot;user&quot;).
                  </Translate>
                </p>
                <p>
                  <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>{' '}
                  <Link to="/account/register" className="alert-link">
                    <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
                  </Link>
                </p>
              </div>
            </Col>
          </>
        )}
      </Col>
    </Row>
  );
};

export default Home;
