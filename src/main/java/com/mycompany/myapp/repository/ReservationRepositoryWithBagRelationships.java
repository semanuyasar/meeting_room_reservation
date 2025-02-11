package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ReservationRepositoryWithBagRelationships {
    Optional<Reservation> fetchBagRelationships(Optional<Reservation> reservation);

    List<Reservation> fetchBagRelationships(List<Reservation> reservations);

    Page<Reservation> fetchBagRelationships(Page<Reservation> reservations);
}
