import React from 'react';
import { Translate } from 'react-jhipster';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { useAppSelector } from 'app/config/store';

const EntitiesMenu = () => {
  const role = useAppSelector(state => state.authentication.account.authorities);

  return (
    <>
      {/* prettier-ignore */}
      {role?.includes('ROLE_ADMIN') ? (
        <MenuItem icon="asterisk" to="/employee">
          <Translate contentKey="global.menu.entities.employee" />
        </MenuItem>
      ) : (
        <></>
      )}
      <MenuItem icon="asterisk" to="/meeting-room">
        <Translate contentKey="global.menu.entities.meetingRoom" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reservation">
        <Translate contentKey="global.menu.entities.reservation" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
