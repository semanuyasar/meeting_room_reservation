package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MeetingRoomTestSamples.*;
import static com.mycompany.myapp.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MeetingRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeetingRoom.class);
        MeetingRoom meetingRoom1 = getMeetingRoomSample1();
        MeetingRoom meetingRoom2 = new MeetingRoom();
        assertThat(meetingRoom1).isNotEqualTo(meetingRoom2);

        meetingRoom2.setId(meetingRoom1.getId());
        assertThat(meetingRoom1).isEqualTo(meetingRoom2);

        meetingRoom2 = getMeetingRoomSample2();
        assertThat(meetingRoom1).isNotEqualTo(meetingRoom2);
    }

    @Test
    void reservationTest() {
        MeetingRoom meetingRoom = getMeetingRoomRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        meetingRoom.addReservation(reservationBack);
        assertThat(meetingRoom.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getEmployees()).containsOnly(meetingRoom);

        meetingRoom.removeReservation(reservationBack);
        assertThat(meetingRoom.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getEmployees()).doesNotContain(meetingRoom);

        meetingRoom.reservations(new HashSet<>(Set.of(reservationBack)));
        assertThat(meetingRoom.getReservations()).containsOnly(reservationBack);
        assertThat(reservationBack.getEmployees()).containsOnly(meetingRoom);

        meetingRoom.setReservations(new HashSet<>());
        assertThat(meetingRoom.getReservations()).doesNotContain(reservationBack);
        assertThat(reservationBack.getEmployees()).doesNotContain(meetingRoom);
    }
}
