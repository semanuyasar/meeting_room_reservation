package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MeetingRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MeetingRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {}
