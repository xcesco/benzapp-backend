package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Fascia;
import it.insiel.innovazione.poc.benzapp.repository.FasciaRepository;
import java.util.List;
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
 * Integration tests for the {@link FasciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FasciaResourceIT {

    private static final String DEFAULT_DESCRIZIONE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIZIONE = "BBBBBBBBBB";

    private static final Float DEFAULT_SCONTO_BENZINA = 1F;
    private static final Float UPDATED_SCONTO_BENZINA = 2F;

    private static final Float DEFAULT_SCONTO_GASOLIO = 1F;
    private static final Float UPDATED_SCONTO_GASOLIO = 2F;

    @Autowired
    private FasciaRepository fasciaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFasciaMockMvc;

    private Fascia fascia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fascia createEntity(EntityManager em) {
        Fascia fascia = new Fascia()
            .descrizione(DEFAULT_DESCRIZIONE)
            .scontoBenzina(DEFAULT_SCONTO_BENZINA)
            .scontoGasolio(DEFAULT_SCONTO_GASOLIO);
        return fascia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fascia createUpdatedEntity(EntityManager em) {
        Fascia fascia = new Fascia()
            .descrizione(UPDATED_DESCRIZIONE)
            .scontoBenzina(UPDATED_SCONTO_BENZINA)
            .scontoGasolio(UPDATED_SCONTO_GASOLIO);
        return fascia;
    }

    @BeforeEach
    public void initTest() {
        fascia = createEntity(em);
    }

    @Test
    @Transactional
    void createFascia() throws Exception {
        int databaseSizeBeforeCreate = fasciaRepository.findAll().size();
        // Create the Fascia
        restFasciaMockMvc
            .perform(post("/api/fascias").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fascia)))
            .andExpect(status().isCreated());

        // Validate the Fascia in the database
        List<Fascia> fasciaList = fasciaRepository.findAll();
        assertThat(fasciaList).hasSize(databaseSizeBeforeCreate + 1);
        Fascia testFascia = fasciaList.get(fasciaList.size() - 1);
        assertThat(testFascia.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
        assertThat(testFascia.getScontoBenzina()).isEqualTo(DEFAULT_SCONTO_BENZINA);
        assertThat(testFascia.getScontoGasolio()).isEqualTo(DEFAULT_SCONTO_GASOLIO);
    }

    @Test
    @Transactional
    void createFasciaWithExistingId() throws Exception {
        // Create the Fascia with an existing ID
        fascia.setId(1L);

        int databaseSizeBeforeCreate = fasciaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFasciaMockMvc
            .perform(post("/api/fascias").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fascia)))
            .andExpect(status().isBadRequest());

        // Validate the Fascia in the database
        List<Fascia> fasciaList = fasciaRepository.findAll();
        assertThat(fasciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFascias() throws Exception {
        // Initialize the database
        fasciaRepository.saveAndFlush(fascia);

        // Get all the fasciaList
        restFasciaMockMvc
            .perform(get("/api/fascias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fascia.getId().intValue())))
            .andExpect(jsonPath("$.[*].descrizione").value(hasItem(DEFAULT_DESCRIZIONE)))
            .andExpect(jsonPath("$.[*].scontoBenzina").value(hasItem(DEFAULT_SCONTO_BENZINA.doubleValue())))
            .andExpect(jsonPath("$.[*].scontoGasolio").value(hasItem(DEFAULT_SCONTO_GASOLIO.doubleValue())));
    }

    @Test
    @Transactional
    void getFascia() throws Exception {
        // Initialize the database
        fasciaRepository.saveAndFlush(fascia);

        // Get the fascia
        restFasciaMockMvc
            .perform(get("/api/fascias/{id}", fascia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fascia.getId().intValue()))
            .andExpect(jsonPath("$.descrizione").value(DEFAULT_DESCRIZIONE))
            .andExpect(jsonPath("$.scontoBenzina").value(DEFAULT_SCONTO_BENZINA.doubleValue()))
            .andExpect(jsonPath("$.scontoGasolio").value(DEFAULT_SCONTO_GASOLIO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFascia() throws Exception {
        // Get the fascia
        restFasciaMockMvc.perform(get("/api/fascias/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateFascia() throws Exception {
        // Initialize the database
        fasciaRepository.saveAndFlush(fascia);

        int databaseSizeBeforeUpdate = fasciaRepository.findAll().size();

        // Update the fascia
        Fascia updatedFascia = fasciaRepository.findById(fascia.getId()).get();
        // Disconnect from session so that the updates on updatedFascia are not directly saved in db
        em.detach(updatedFascia);
        updatedFascia.descrizione(UPDATED_DESCRIZIONE).scontoBenzina(UPDATED_SCONTO_BENZINA).scontoGasolio(UPDATED_SCONTO_GASOLIO);

        restFasciaMockMvc
            .perform(put("/api/fascias").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedFascia)))
            .andExpect(status().isOk());

        // Validate the Fascia in the database
        List<Fascia> fasciaList = fasciaRepository.findAll();
        assertThat(fasciaList).hasSize(databaseSizeBeforeUpdate);
        Fascia testFascia = fasciaList.get(fasciaList.size() - 1);
        assertThat(testFascia.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testFascia.getScontoBenzina()).isEqualTo(UPDATED_SCONTO_BENZINA);
        assertThat(testFascia.getScontoGasolio()).isEqualTo(UPDATED_SCONTO_GASOLIO);
    }

    @Test
    @Transactional
    void updateNonExistingFascia() throws Exception {
        int databaseSizeBeforeUpdate = fasciaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFasciaMockMvc
            .perform(put("/api/fascias").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fascia)))
            .andExpect(status().isBadRequest());

        // Validate the Fascia in the database
        List<Fascia> fasciaList = fasciaRepository.findAll();
        assertThat(fasciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFasciaWithPatch() throws Exception {
        // Initialize the database
        fasciaRepository.saveAndFlush(fascia);

        int databaseSizeBeforeUpdate = fasciaRepository.findAll().size();

        // Update the fascia using partial update
        Fascia partialUpdatedFascia = new Fascia();
        partialUpdatedFascia.setId(fascia.getId());

        partialUpdatedFascia.scontoGasolio(UPDATED_SCONTO_GASOLIO);

        restFasciaMockMvc
            .perform(
                patch("/api/fascias")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFascia))
            )
            .andExpect(status().isOk());

        // Validate the Fascia in the database
        List<Fascia> fasciaList = fasciaRepository.findAll();
        assertThat(fasciaList).hasSize(databaseSizeBeforeUpdate);
        Fascia testFascia = fasciaList.get(fasciaList.size() - 1);
        assertThat(testFascia.getDescrizione()).isEqualTo(DEFAULT_DESCRIZIONE);
        assertThat(testFascia.getScontoBenzina()).isEqualTo(DEFAULT_SCONTO_BENZINA);
        assertThat(testFascia.getScontoGasolio()).isEqualTo(UPDATED_SCONTO_GASOLIO);
    }

    @Test
    @Transactional
    void fullUpdateFasciaWithPatch() throws Exception {
        // Initialize the database
        fasciaRepository.saveAndFlush(fascia);

        int databaseSizeBeforeUpdate = fasciaRepository.findAll().size();

        // Update the fascia using partial update
        Fascia partialUpdatedFascia = new Fascia();
        partialUpdatedFascia.setId(fascia.getId());

        partialUpdatedFascia.descrizione(UPDATED_DESCRIZIONE).scontoBenzina(UPDATED_SCONTO_BENZINA).scontoGasolio(UPDATED_SCONTO_GASOLIO);

        restFasciaMockMvc
            .perform(
                patch("/api/fascias")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFascia))
            )
            .andExpect(status().isOk());

        // Validate the Fascia in the database
        List<Fascia> fasciaList = fasciaRepository.findAll();
        assertThat(fasciaList).hasSize(databaseSizeBeforeUpdate);
        Fascia testFascia = fasciaList.get(fasciaList.size() - 1);
        assertThat(testFascia.getDescrizione()).isEqualTo(UPDATED_DESCRIZIONE);
        assertThat(testFascia.getScontoBenzina()).isEqualTo(UPDATED_SCONTO_BENZINA);
        assertThat(testFascia.getScontoGasolio()).isEqualTo(UPDATED_SCONTO_GASOLIO);
    }

    @Test
    @Transactional
    void partialUpdateFasciaShouldThrown() throws Exception {
        // Update the fascia without id should throw
        Fascia partialUpdatedFascia = new Fascia();

        restFasciaMockMvc
            .perform(
                patch("/api/fascias")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFascia))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteFascia() throws Exception {
        // Initialize the database
        fasciaRepository.saveAndFlush(fascia);

        int databaseSizeBeforeDelete = fasciaRepository.findAll().size();

        // Delete the fascia
        restFasciaMockMvc
            .perform(delete("/api/fascias/{id}", fascia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fascia> fasciaList = fasciaRepository.findAll();
        assertThat(fasciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
