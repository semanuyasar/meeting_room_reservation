package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ReservationRepositoryWithBagRelationshipsImpl implements ReservationRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String RESERVATIONS_PARAMETER = "reservations";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reservation> fetchBagRelationships(Optional<Reservation> reservation) {
        return reservation.map(this::fetchEmployees);
    }

    @Override
    public Page<Reservation> fetchBagRelationships(Page<Reservation> reservations) {
        return new PageImpl<>(
            fetchBagRelationships(reservations.getContent()),
            reservations.getPageable(),
            reservations.getTotalElements()
        );
    }

    @Override
    public List<Reservation> fetchBagRelationships(List<Reservation> reservations) {
        return Optional.of(reservations).map(this::fetchEmployees).orElse(Collections.emptyList());
    }

    Reservation fetchEmployees(Reservation result) {
        return entityManager
            .createQuery(
                "select reservation from Reservation reservation left join fetch reservation.employees where reservation.id = :id",
                Reservation.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Reservation> fetchEmployees(List<Reservation> reservations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, reservations.size()).forEach(index -> order.put(reservations.get(index).getId(), index));
        List<Reservation> result = entityManager
            .createQuery(
                "select reservation from Reservation reservation left join fetch reservation.employees where reservation in :reservations",
                Reservation.class
            )
            .setParameter(RESERVATIONS_PARAMETER, reservations)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
