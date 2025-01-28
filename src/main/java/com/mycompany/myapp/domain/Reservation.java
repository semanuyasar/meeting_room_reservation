package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_time")
    private LocalDate startTime;

    @Column(name = "end_time")
    private LocalDate endTime;

    @Column(name = "duration")
    private Duration duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reservations" }, allowSetters = true)
    private MeetingRoom room;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_reservation__employee",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reservations" }, allowSetters = true)
    private Set<MeetingRoom> employees = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartTime() {
        return this.startTime;
    }

    public Reservation startTime(LocalDate startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return this.endTime;
    }

    public Reservation endTime(LocalDate endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Reservation duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public MeetingRoom getRoom() {
        return this.room;
    }

    public void setRoom(MeetingRoom meetingRoom) {
        this.room = meetingRoom;
    }

    public Reservation room(MeetingRoom meetingRoom) {
        this.setRoom(meetingRoom);
        return this;
    }

    public Set<MeetingRoom> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<MeetingRoom> meetingRooms) {
        this.employees = meetingRooms;
    }

    public Reservation employees(Set<MeetingRoom> meetingRooms) {
        this.setEmployees(meetingRooms);
        return this;
    }

    public Reservation addEmployee(MeetingRoom meetingRoom) {
        this.employees.add(meetingRoom);
        return this;
    }

    public Reservation removeEmployee(MeetingRoom meetingRoom) {
        this.employees.remove(meetingRoom);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Reservation owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return getId() != null && getId().equals(((Reservation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
