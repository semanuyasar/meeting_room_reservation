package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Reservation;
import com.mycompany.myapp.repository.ReservationRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Reservation}.
 */
@RestController
@RequestMapping("/api/reservations")
@Transactional
public class ReservationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationResource.class);

    private static final String ENTITY_NAME = "reservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservationRepository reservationRepository;

    public ReservationResource(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * {@code POST  /reservations} : Create a new reservation.
     *
     * @param reservation the reservation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservation, or with status {@code 400 (Bad Request)} if the reservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) throws URISyntaxException {
        LOG.debug("REST request to save Reservation : {}", reservation);
        if (reservation.getId() != null) {
            throw new BadRequestAlertException("A new reservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reservation = reservationRepository.save(reservation);
        return ResponseEntity.created(new URI("/api/reservations/" + reservation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reservation.getId().toString()))
            .body(reservation);
    }

    /**
     * {@code PUT  /reservations/:id} : Updates an existing reservation.
     *
     * @param id the id of the reservation to save.
     * @param reservation the reservation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservation,
     * or with status {@code 400 (Bad Request)} if the reservation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reservation reservation
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reservation : {}, {}", id, reservation);
        if (reservation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reservation = reservationRepository.save(reservation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservation.getId().toString()))
            .body(reservation);
    }

    /**
     * {@code PATCH  /reservations/:id} : Partial updates given fields of an existing reservation, field will ignore if it is null
     *
     * @param id the id of the reservation to save.
     * @param reservation the reservation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservation,
     * or with status {@code 400 (Bad Request)} if the reservation is not valid,
     * or with status {@code 404 (Not Found)} if the reservation is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Reservation> partialUpdateReservation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reservation reservation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reservation partially : {}, {}", id, reservation);
        if (reservation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Reservation> result = reservationRepository
            .findById(reservation.getId())
            .map(existingReservation -> {
                if (reservation.getStartTime() != null) {
                    existingReservation.setStartTime(reservation.getStartTime());
                }
                if (reservation.getEndTime() != null) {
                    existingReservation.setEndTime(reservation.getEndTime());
                }
                if (reservation.getDuration() != null) {
                    existingReservation.setDuration(reservation.getDuration());
                }

                return existingReservation;
            })
            .map(reservationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservation.getId().toString())
        );
    }

    /**
     * {@code GET  /reservations} : get all the reservations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservations in body.
     */
    @GetMapping("")
    public List<Reservation> getAllReservations(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Reservations");
        if (eagerload) {
            return reservationRepository.findAllWithEagerRelationships();
        } else {
            return reservationRepository.findAll();
        }
    }

    /**
     * {@code GET  /reservations/:id} : get the "id" reservation.
     *
     * @param id the id of the reservation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Reservation : {}", id);
        Optional<Reservation> reservation = reservationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(reservation);
    }

    /**
     * {@code DELETE  /reservations/:id} : delete the "id" reservation.
     *
     * @param id the id of the reservation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
