package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MeetingRoomAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MeetingRoom;
import com.mycompany.myapp.repository.MeetingRoomRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MeetingRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeetingRoomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    private static final String ENTITY_API_URL = "/api/meeting-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMeetingRoomMockMvc;

    private MeetingRoom meetingRoom;

    private MeetingRoom insertedMeetingRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeetingRoom createEntity() {
        return new MeetingRoom().name(DEFAULT_NAME).location(DEFAULT_LOCATION).capacity(DEFAULT_CAPACITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MeetingRoom createUpdatedEntity() {
        return new MeetingRoom().name(UPDATED_NAME).location(UPDATED_LOCATION).capacity(UPDATED_CAPACITY);
    }

    @BeforeEach
    public void initTest() {
        meetingRoom = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMeetingRoom != null) {
            meetingRoomRepository.delete(insertedMeetingRoom);
            insertedMeetingRoom = null;
        }
    }

    @Test
    @Transactional
    void createMeetingRoom() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MeetingRoom
        var returnedMeetingRoom = om.readValue(
            restMeetingRoomMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meetingRoom)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MeetingRoom.class
        );

        // Validate the MeetingRoom in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMeetingRoomUpdatableFieldsEquals(returnedMeetingRoom, getPersistedMeetingRoom(returnedMeetingRoom));

        insertedMeetingRoom = returnedMeetingRoom;
    }

    @Test
    @Transactional
    void createMeetingRoomWithExistingId() throws Exception {
        // Create the MeetingRoom with an existing ID
        meetingRoom.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeetingRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meetingRoom)))
            .andExpect(status().isBadRequest());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMeetingRooms() throws Exception {
        // Initialize the database
        insertedMeetingRoom = meetingRoomRepository.saveAndFlush(meetingRoom);

        // Get all the meetingRoomList
        restMeetingRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meetingRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }

    @Test
    @Transactional
    void getMeetingRoom() throws Exception {
        // Initialize the database
        insertedMeetingRoom = meetingRoomRepository.saveAndFlush(meetingRoom);

        // Get the meetingRoom
        restMeetingRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, meetingRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meetingRoom.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY));
    }

    @Test
    @Transactional
    void getNonExistingMeetingRoom() throws Exception {
        // Get the meetingRoom
        restMeetingRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMeetingRoom() throws Exception {
        // Initialize the database
        insertedMeetingRoom = meetingRoomRepository.saveAndFlush(meetingRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the meetingRoom
        MeetingRoom updatedMeetingRoom = meetingRoomRepository.findById(meetingRoom.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMeetingRoom are not directly saved in db
        em.detach(updatedMeetingRoom);
        updatedMeetingRoom.name(UPDATED_NAME).location(UPDATED_LOCATION).capacity(UPDATED_CAPACITY);

        restMeetingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMeetingRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMeetingRoom))
            )
            .andExpect(status().isOk());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMeetingRoomToMatchAllProperties(updatedMeetingRoom);
    }

    @Test
    @Transactional
    void putNonExistingMeetingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meetingRoom.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, meetingRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(meetingRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMeetingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meetingRoom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(meetingRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMeetingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meetingRoom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(meetingRoom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMeetingRoomWithPatch() throws Exception {
        // Initialize the database
        insertedMeetingRoom = meetingRoomRepository.saveAndFlush(meetingRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the meetingRoom using partial update
        MeetingRoom partialUpdatedMeetingRoom = new MeetingRoom();
        partialUpdatedMeetingRoom.setId(meetingRoom.getId());

        partialUpdatedMeetingRoom.name(UPDATED_NAME).location(UPDATED_LOCATION).capacity(UPDATED_CAPACITY);

        restMeetingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeetingRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMeetingRoom))
            )
            .andExpect(status().isOk());

        // Validate the MeetingRoom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMeetingRoomUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMeetingRoom, meetingRoom),
            getPersistedMeetingRoom(meetingRoom)
        );
    }

    @Test
    @Transactional
    void fullUpdateMeetingRoomWithPatch() throws Exception {
        // Initialize the database
        insertedMeetingRoom = meetingRoomRepository.saveAndFlush(meetingRoom);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the meetingRoom using partial update
        MeetingRoom partialUpdatedMeetingRoom = new MeetingRoom();
        partialUpdatedMeetingRoom.setId(meetingRoom.getId());

        partialUpdatedMeetingRoom.name(UPDATED_NAME).location(UPDATED_LOCATION).capacity(UPDATED_CAPACITY);

        restMeetingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMeetingRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMeetingRoom))
            )
            .andExpect(status().isOk());

        // Validate the MeetingRoom in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMeetingRoomUpdatableFieldsEquals(partialUpdatedMeetingRoom, getPersistedMeetingRoom(partialUpdatedMeetingRoom));
    }

    @Test
    @Transactional
    void patchNonExistingMeetingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meetingRoom.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMeetingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, meetingRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(meetingRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMeetingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meetingRoom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(meetingRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMeetingRoom() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        meetingRoom.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeetingRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(meetingRoom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MeetingRoom in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMeetingRoom() throws Exception {
        // Initialize the database
        insertedMeetingRoom = meetingRoomRepository.saveAndFlush(meetingRoom);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the meetingRoom
        restMeetingRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, meetingRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return meetingRoomRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MeetingRoom getPersistedMeetingRoom(MeetingRoom meetingRoom) {
        return meetingRoomRepository.findById(meetingRoom.getId()).orElseThrow();
    }

    protected void assertPersistedMeetingRoomToMatchAllProperties(MeetingRoom expectedMeetingRoom) {
        assertMeetingRoomAllPropertiesEquals(expectedMeetingRoom, getPersistedMeetingRoom(expectedMeetingRoom));
    }

    protected void assertPersistedMeetingRoomToMatchUpdatableProperties(MeetingRoom expectedMeetingRoom) {
        assertMeetingRoomAllUpdatablePropertiesEquals(expectedMeetingRoom, getPersistedMeetingRoom(expectedMeetingRoom));
    }
}
