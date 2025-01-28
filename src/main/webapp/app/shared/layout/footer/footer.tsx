import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>
          <Translate contentKey="footer">footer</Translate>
          <span style={{ color: '#dc350d' }}>
            <a style={{ color: '#dc350d', fontWeight: 600 }} target="_blank" rel="noopener noreferrer">
              Koç
            </a>
          </span>
          <span>
            <span style={{ color: '#0d6bdc' }}>&nbsp;</span>
            Copyright © 2025
            <span style={{ color: '#0d6bdc' }}> Düzey A.Ş.</span>
          </span>
        </p>
      </Col>
    </Row>
  </div>
);

export default Footer;
