package it.insiel.innovazione.poc.benzapp.web.rest;

import static it.insiel.innovazione.poc.benzapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Notifica;
import it.insiel.innovazione.poc.benzapp.repository.NotificaRepository;
import it.insiel.innovazione.poc.benzapp.service.criteria.NotificaCriteria;
import it.insiel.innovazione.poc.benzapp.service.dto.NotificaDTO;
import it.insiel.innovazione.poc.benzapp.service.mapper.NotificaMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NotificaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificaResourceIT {

    private static final String DEFAULT_TARGA = "AAAAAAAAAA";
    private static final String UPDATED_TARGA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/notificas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificaRepository notificaRepository;

    @Autowired
    private NotificaMapper notificaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificaMockMvc;

    private Notifica notifica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notifica createEntity(EntityManager em) {
        Notifica notifica = new Notifica().targa(DEFAULT_TARGA).data(DEFAULT_DATA);
        return notifica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notifica createUpdatedEntity(EntityManager em) {
        Notifica notifica = new Notifica().targa(UPDATED_TARGA).data(UPDATED_DATA);
        return notifica;
    }

    @BeforeEach
    public void initTest() {
        notifica = createEntity(em);
    }

    @Test
    @Transactional
    void createNotifica() throws Exception {
        int databaseSizeBeforeCreate = notificaRepository.findAll().size();
        // Create the Notifica
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);
        restNotificaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificaDTO)))
            .andExpect(status().isCreated());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeCreate + 1);
        Notifica testNotifica = notificaList.get(notificaList.size() - 1);
        assertThat(testNotifica.getTarga()).isEqualTo(DEFAULT_TARGA);
        assertThat(testNotifica.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void createNotificaWithExistingId() throws Exception {
        // Create the Notifica with an existing ID
        notifica.setId(1L);
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        int databaseSizeBeforeCreate = notificaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTargaIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificaRepository.findAll().size();
        // set the field null
        notifica.setTarga(null);

        // Create the Notifica, which fails.
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        restNotificaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificaDTO)))
            .andExpect(status().isBadRequest());

        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificaRepository.findAll().size();
        // set the field null
        notifica.setData(null);

        // Create the Notifica, which fails.
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        restNotificaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificaDTO)))
            .andExpect(status().isBadRequest());

        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificas() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList
        restNotificaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notifica.getId().intValue())))
            .andExpect(jsonPath("$.[*].targa").value(hasItem(DEFAULT_TARGA)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));
    }

    @Test
    @Transactional
    void getNotifica() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get the notifica
        restNotificaMockMvc
            .perform(get(ENTITY_API_URL_ID, notifica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notifica.getId().intValue()))
            .andExpect(jsonPath("$.targa").value(DEFAULT_TARGA))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    void getNotificasByIdFiltering() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        Long id = notifica.getId();

        defaultNotificaShouldBeFound("id.equals=" + id);
        defaultNotificaShouldNotBeFound("id.notEquals=" + id);

        defaultNotificaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificaShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificasByTargaIsEqualToSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where targa equals to DEFAULT_TARGA
        defaultNotificaShouldBeFound("targa.equals=" + DEFAULT_TARGA);

        // Get all the notificaList where targa equals to UPDATED_TARGA
        defaultNotificaShouldNotBeFound("targa.equals=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllNotificasByTargaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where targa not equals to DEFAULT_TARGA
        defaultNotificaShouldNotBeFound("targa.notEquals=" + DEFAULT_TARGA);

        // Get all the notificaList where targa not equals to UPDATED_TARGA
        defaultNotificaShouldBeFound("targa.notEquals=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllNotificasByTargaIsInShouldWork() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where targa in DEFAULT_TARGA or UPDATED_TARGA
        defaultNotificaShouldBeFound("targa.in=" + DEFAULT_TARGA + "," + UPDATED_TARGA);

        // Get all the notificaList where targa equals to UPDATED_TARGA
        defaultNotificaShouldNotBeFound("targa.in=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllNotificasByTargaIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where targa is not null
        defaultNotificaShouldBeFound("targa.specified=true");

        // Get all the notificaList where targa is null
        defaultNotificaShouldNotBeFound("targa.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificasByTargaContainsSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where targa contains DEFAULT_TARGA
        defaultNotificaShouldBeFound("targa.contains=" + DEFAULT_TARGA);

        // Get all the notificaList where targa contains UPDATED_TARGA
        defaultNotificaShouldNotBeFound("targa.contains=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllNotificasByTargaNotContainsSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where targa does not contain DEFAULT_TARGA
        defaultNotificaShouldNotBeFound("targa.doesNotContain=" + DEFAULT_TARGA);

        // Get all the notificaList where targa does not contain UPDATED_TARGA
        defaultNotificaShouldBeFound("targa.doesNotContain=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data equals to DEFAULT_DATA
        defaultNotificaShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the notificaList where data equals to UPDATED_DATA
        defaultNotificaShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data not equals to DEFAULT_DATA
        defaultNotificaShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the notificaList where data not equals to UPDATED_DATA
        defaultNotificaShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsInShouldWork() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data in DEFAULT_DATA or UPDATED_DATA
        defaultNotificaShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the notificaList where data equals to UPDATED_DATA
        defaultNotificaShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data is not null
        defaultNotificaShouldBeFound("data.specified=true");

        // Get all the notificaList where data is null
        defaultNotificaShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data is greater than or equal to DEFAULT_DATA
        defaultNotificaShouldBeFound("data.greaterThanOrEqual=" + DEFAULT_DATA);

        // Get all the notificaList where data is greater than or equal to UPDATED_DATA
        defaultNotificaShouldNotBeFound("data.greaterThanOrEqual=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data is less than or equal to DEFAULT_DATA
        defaultNotificaShouldBeFound("data.lessThanOrEqual=" + DEFAULT_DATA);

        // Get all the notificaList where data is less than or equal to SMALLER_DATA
        defaultNotificaShouldNotBeFound("data.lessThanOrEqual=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsLessThanSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data is less than DEFAULT_DATA
        defaultNotificaShouldNotBeFound("data.lessThan=" + DEFAULT_DATA);

        // Get all the notificaList where data is less than UPDATED_DATA
        defaultNotificaShouldBeFound("data.lessThan=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllNotificasByDataIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        // Get all the notificaList where data is greater than DEFAULT_DATA
        defaultNotificaShouldNotBeFound("data.greaterThan=" + DEFAULT_DATA);

        // Get all the notificaList where data is greater than SMALLER_DATA
        defaultNotificaShouldBeFound("data.greaterThan=" + SMALLER_DATA);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificaShouldBeFound(String filter) throws Exception {
        restNotificaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notifica.getId().intValue())))
            .andExpect(jsonPath("$.[*].targa").value(hasItem(DEFAULT_TARGA)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));

        // Check, that the count call also returns 1
        restNotificaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificaShouldNotBeFound(String filter) throws Exception {
        restNotificaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotifica() throws Exception {
        // Get the notifica
        restNotificaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNotifica() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();

        // Update the notifica
        Notifica updatedNotifica = notificaRepository.findById(notifica.getId()).get();
        // Disconnect from session so that the updates on updatedNotifica are not directly saved in db
        em.detach(updatedNotifica);
        updatedNotifica.targa(UPDATED_TARGA).data(UPDATED_DATA);
        NotificaDTO notificaDTO = notificaMapper.toDto(updatedNotifica);

        restNotificaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
        Notifica testNotifica = notificaList.get(notificaList.size() - 1);
        assertThat(testNotifica.getTarga()).isEqualTo(UPDATED_TARGA);
        assertThat(testNotifica.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void putNonExistingNotifica() throws Exception {
        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();
        notifica.setId(count.incrementAndGet());

        // Create the Notifica
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotifica() throws Exception {
        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();
        notifica.setId(count.incrementAndGet());

        // Create the Notifica
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotifica() throws Exception {
        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();
        notifica.setId(count.incrementAndGet());

        // Create the Notifica
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificaWithPatch() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();

        // Update the notifica using partial update
        Notifica partialUpdatedNotifica = new Notifica();
        partialUpdatedNotifica.setId(notifica.getId());

        partialUpdatedNotifica.targa(UPDATED_TARGA);

        restNotificaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotifica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotifica))
            )
            .andExpect(status().isOk());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
        Notifica testNotifica = notificaList.get(notificaList.size() - 1);
        assertThat(testNotifica.getTarga()).isEqualTo(UPDATED_TARGA);
        assertThat(testNotifica.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void fullUpdateNotificaWithPatch() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();

        // Update the notifica using partial update
        Notifica partialUpdatedNotifica = new Notifica();
        partialUpdatedNotifica.setId(notifica.getId());

        partialUpdatedNotifica.targa(UPDATED_TARGA).data(UPDATED_DATA);

        restNotificaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotifica.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotifica))
            )
            .andExpect(status().isOk());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
        Notifica testNotifica = notificaList.get(notificaList.size() - 1);
        assertThat(testNotifica.getTarga()).isEqualTo(UPDATED_TARGA);
        assertThat(testNotifica.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingNotifica() throws Exception {
        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();
        notifica.setId(count.incrementAndGet());

        // Create the Notifica
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotifica() throws Exception {
        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();
        notifica.setId(count.incrementAndGet());

        // Create the Notifica
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotifica() throws Exception {
        int databaseSizeBeforeUpdate = notificaRepository.findAll().size();
        notifica.setId(count.incrementAndGet());

        // Create the Notifica
        NotificaDTO notificaDTO = notificaMapper.toDto(notifica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(notificaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notifica in the database
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotifica() throws Exception {
        // Initialize the database
        notificaRepository.saveAndFlush(notifica);

        int databaseSizeBeforeDelete = notificaRepository.findAll().size();

        // Delete the notifica
        restNotificaMockMvc
            .perform(delete(ENTITY_API_URL_ID, notifica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notifica> notificaList = notificaRepository.findAll();
        assertThat(notificaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
