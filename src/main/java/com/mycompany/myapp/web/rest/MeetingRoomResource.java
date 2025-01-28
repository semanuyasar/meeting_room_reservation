package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.MeetingRoom;
import com.mycompany.myapp.repository.MeetingRoomRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.MeetingRoom}.
 */
@RestController
@RequestMapping("/api/meeting-rooms")
@Transactional
public class MeetingRoomResource {

    private static final Logger LOG = LoggerFactory.getLogger(MeetingRoomResource.class);

    private static final String ENTITY_NAME = "meetingRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeetingRoomRepository meetingRoomRepository;

    public MeetingRoomResource(MeetingRoomRepository meetingRoomRepository) {
        this.meetingRoomRepository = meetingRoomRepository;
    }

    /**
     * {@code POST  /meeting-rooms} : Create a new meetingRoom.
     *
     * @param meetingRoom the meetingRoom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new meetingRoom, or with status {@code 400 (Bad Request)} if the meetingRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MeetingRoom> createMeetingRoom(@RequestBody MeetingRoom meetingRoom) throws URISyntaxException {
        LOG.debug("REST request to save MeetingRoom : {}", meetingRoom);
        if (meetingRoom.getId() != null) {
            throw new BadRequestAlertException("A new meetingRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        meetingRoom = meetingRoomRepository.save(meetingRoom);
        return ResponseEntity.created(new URI("/api/meeting-rooms/" + meetingRoom.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, meetingRoom.getId().toString()))
            .body(meetingRoom);
    }

    /**
     * {@code PUT  /meeting-rooms/:id} : Updates an existing meetingRoom.
     *
     * @param id the id of the meetingRoom to save.
     * @param meetingRoom the meetingRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meetingRoom,
     * or with status {@code 400 (Bad Request)} if the meetingRoom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the meetingRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MeetingRoom> updateMeetingRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeetingRoom meetingRoom
    ) throws URISyntaxException {
        LOG.debug("REST request to update MeetingRoom : {}, {}", id, meetingRoom);
        if (meetingRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meetingRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        meetingRoom = meetingRoomRepository.save(meetingRoom);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meetingRoom.getId().toString()))
            .body(meetingRoom);
    }

    /**
     * {@code PATCH  /meeting-rooms/:id} : Partial updates given fields of an existing meetingRoom, field will ignore if it is null
     *
     * @param id the id of the meetingRoom to save.
     * @param meetingRoom the meetingRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meetingRoom,
     * or with status {@code 400 (Bad Request)} if the meetingRoom is not valid,
     * or with status {@code 404 (Not Found)} if the meetingRoom is not found,
     * or with status {@code 500 (Internal Server Error)} if the meetingRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MeetingRoom> partialUpdateMeetingRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MeetingRoom meetingRoom
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MeetingRoom partially : {}, {}", id, meetingRoom);
        if (meetingRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, meetingRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!meetingRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MeetingRoom> result = meetingRoomRepository
            .findById(meetingRoom.getId())
            .map(existingMeetingRoom -> {
                if (meetingRoom.getName() != null) {
                    existingMeetingRoom.setName(meetingRoom.getName());
                }
                if (meetingRoom.getLocation() != null) {
                    existingMeetingRoom.setLocation(meetingRoom.getLocation());
                }
                if (meetingRoom.getCapacity() != null) {
                    existingMeetingRoom.setCapacity(meetingRoom.getCapacity());
                }

                return existingMeetingRoom;
            })
            .map(meetingRoomRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meetingRoom.getId().toString())
        );
    }

    /**
     * {@code GET  /meeting-rooms} : get all the meetingRooms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of meetingRooms in body.
     */
    @GetMapping("")
    public List<MeetingRoom> getAllMeetingRooms() {
        LOG.debug("REST request to get all MeetingRooms");
        return meetingRoomRepository.findAll();
    }

    /**
     * {@code GET  /meeting-rooms/:id} : get the "id" meetingRoom.
     *
     * @param id the id of the meetingRoom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the meetingRoom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MeetingRoom> getMeetingRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MeetingRoom : {}", id);
        Optional<MeetingRoom> meetingRoom = meetingRoomRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(meetingRoom);
    }

    /**
     * {@code DELETE  /meeting-rooms/:id} : delete the "id" meetingRoom.
     *
     * @param id the id of the meetingRoom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MeetingRoom : {}", id);
        meetingRoomRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
