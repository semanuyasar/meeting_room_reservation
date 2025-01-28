package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MeetingRoomTestSamples.*;
import static com.mycompany.myapp.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = getReservationSample1();
        Reservation reservation2 = new Reservation();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationSample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void roomTest() {
        Reservation reservation = getReservationRandomSampleGenerator();
        MeetingRoom meetingRoomBack = getMeetingRoomRandomSampleGenerator();

        reservation.setRoom(meetingRoomBack);
        assertThat(reservation.getRoom()).isEqualTo(meetingRoomBack);

        reservation.room(null);
        assertThat(reservation.getRoom()).isNull();
    }

    @Test
    void employeeTest() {
        Reservation reservation = getReservationRandomSampleGenerator();
        MeetingRoom meetingRoomBack = getMeetingRoomRandomSampleGenerator();

        reservation.addEmployee(meetingRoomBack);
        assertThat(reservation.getEmployees()).containsOnly(meetingRoomBack);

        reservation.removeEmployee(meetingRoomBack);
        assertThat(reservation.getEmployees()).doesNotContain(meetingRoomBack);

        reservation.employees(new HashSet<>(Set.of(meetingRoomBack)));
        assertThat(reservation.getEmployees()).containsOnly(meetingRoomBack);

        reservation.setEmployees(new HashSet<>());
        assertThat(reservation.getEmployees()).doesNotContain(meetingRoomBack);
    }
}
