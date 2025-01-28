package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reservation entity.
 *
 * When extending this class, extend ReservationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ReservationRepository extends ReservationRepositoryWithBagRelationships, JpaRepository<Reservation, Long> {
    @Query("select reservation from Reservation reservation where reservation.owner.login = ?#{authentication.name}")
    List<Reservation> findByOwnerIsCurrentUser();

    default Optional<Reservation> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Reservation> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Reservation> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select reservation from Reservation reservation left join fetch reservation.owner",
        countQuery = "select count(reservation) from Reservation reservation"
    )
    Page<Reservation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reservation from Reservation reservation left join fetch reservation.owner")
    List<Reservation> findAllWithToOneRelationships();

    @Query("select reservation from Reservation reservation left join fetch reservation.owner where reservation.id =:id")
    Optional<Reservation> findOneWithToOneRelationships(@Param("id") Long id);
}
