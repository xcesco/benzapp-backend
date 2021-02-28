package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Gestore;
import it.insiel.innovazione.poc.benzapp.repository.GestoreRepository;
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
 * Integration tests for the {@link GestoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GestoreResourceIT {

    private static final String DEFAULT_PROVINCIA = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCIA = "BBBBBBBBBB";

    private static final String DEFAULT_COMUNE = "AAAAAAAAAA";
    private static final String UPDATED_COMUNE = "BBBBBBBBBB";

    private static final String DEFAULT_INDIRIZZO = "AAAAAAAAAA";
    private static final String UPDATED_INDIRIZZO = "BBBBBBBBBB";

    private static final Float DEFAULT_LONGITUDINE = 1F;
    private static final Float UPDATED_LONGITUDINE = 2F;

    private static final Float DEFAULT_LATITUDINE = 1F;
    private static final Float UPDATED_LATITUDINE = 2F;

    private static final String DEFAULT_MARCHIO = "AAAAAAAAAA";
    private static final String UPDATED_MARCHIO = "BBBBBBBBBB";

    @Autowired
    private GestoreRepository gestoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGestoreMockMvc;

    private Gestore gestore;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gestore createEntity(EntityManager em) {
        Gestore gestore = new Gestore()
            .provincia(DEFAULT_PROVINCIA)
            .comune(DEFAULT_COMUNE)
            .indirizzo(DEFAULT_INDIRIZZO)
            .longitudine(DEFAULT_LONGITUDINE)
            .latitudine(DEFAULT_LATITUDINE)
            .marchio(DEFAULT_MARCHIO);
        return gestore;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gestore createUpdatedEntity(EntityManager em) {
        Gestore gestore = new Gestore()
            .provincia(UPDATED_PROVINCIA)
            .comune(UPDATED_COMUNE)
            .indirizzo(UPDATED_INDIRIZZO)
            .longitudine(UPDATED_LONGITUDINE)
            .latitudine(UPDATED_LATITUDINE)
            .marchio(UPDATED_MARCHIO);
        return gestore;
    }

    @BeforeEach
    public void initTest() {
        gestore = createEntity(em);
    }

    @Test
    @Transactional
    void createGestore() throws Exception {
        int databaseSizeBeforeCreate = gestoreRepository.findAll().size();
        // Create the Gestore
        restGestoreMockMvc
            .perform(post("/api/gestores").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gestore)))
            .andExpect(status().isCreated());

        // Validate the Gestore in the database
        List<Gestore> gestoreList = gestoreRepository.findAll();
        assertThat(gestoreList).hasSize(databaseSizeBeforeCreate + 1);
        Gestore testGestore = gestoreList.get(gestoreList.size() - 1);
        assertThat(testGestore.getProvincia()).isEqualTo(DEFAULT_PROVINCIA);
        assertThat(testGestore.getComune()).isEqualTo(DEFAULT_COMUNE);
        assertThat(testGestore.getIndirizzo()).isEqualTo(DEFAULT_INDIRIZZO);
        assertThat(testGestore.getLongitudine()).isEqualTo(DEFAULT_LONGITUDINE);
        assertThat(testGestore.getLatitudine()).isEqualTo(DEFAULT_LATITUDINE);
        assertThat(testGestore.getMarchio()).isEqualTo(DEFAULT_MARCHIO);
    }

    @Test
    @Transactional
    void createGestoreWithExistingId() throws Exception {
        // Create the Gestore with an existing ID
        gestore.setId(1L);

        int databaseSizeBeforeCreate = gestoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGestoreMockMvc
            .perform(post("/api/gestores").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gestore)))
            .andExpect(status().isBadRequest());

        // Validate the Gestore in the database
        List<Gestore> gestoreList = gestoreRepository.findAll();
        assertThat(gestoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGestores() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList
        restGestoreMockMvc
            .perform(get("/api/gestores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestore.getId().intValue())))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)))
            .andExpect(jsonPath("$.[*].comune").value(hasItem(DEFAULT_COMUNE)))
            .andExpect(jsonPath("$.[*].indirizzo").value(hasItem(DEFAULT_INDIRIZZO)))
            .andExpect(jsonPath("$.[*].longitudine").value(hasItem(DEFAULT_LONGITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitudine").value(hasItem(DEFAULT_LATITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].marchio").value(hasItem(DEFAULT_MARCHIO)));
    }

    @Test
    @Transactional
    void getGestore() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get the gestore
        restGestoreMockMvc
            .perform(get("/api/gestores/{id}", gestore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gestore.getId().intValue()))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA))
            .andExpect(jsonPath("$.comune").value(DEFAULT_COMUNE))
            .andExpect(jsonPath("$.indirizzo").value(DEFAULT_INDIRIZZO))
            .andExpect(jsonPath("$.longitudine").value(DEFAULT_LONGITUDINE.doubleValue()))
            .andExpect(jsonPath("$.latitudine").value(DEFAULT_LATITUDINE.doubleValue()))
            .andExpect(jsonPath("$.marchio").value(DEFAULT_MARCHIO));
    }

    @Test
    @Transactional
    void getNonExistingGestore() throws Exception {
        // Get the gestore
        restGestoreMockMvc.perform(get("/api/gestores/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateGestore() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        int databaseSizeBeforeUpdate = gestoreRepository.findAll().size();

        // Update the gestore
        Gestore updatedGestore = gestoreRepository.findById(gestore.getId()).get();
        // Disconnect from session so that the updates on updatedGestore are not directly saved in db
        em.detach(updatedGestore);
        updatedGestore
            .provincia(UPDATED_PROVINCIA)
            .comune(UPDATED_COMUNE)
            .indirizzo(UPDATED_INDIRIZZO)
            .longitudine(UPDATED_LONGITUDINE)
            .latitudine(UPDATED_LATITUDINE)
            .marchio(UPDATED_MARCHIO);

        restGestoreMockMvc
            .perform(
                put("/api/gestores").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedGestore))
            )
            .andExpect(status().isOk());

        // Validate the Gestore in the database
        List<Gestore> gestoreList = gestoreRepository.findAll();
        assertThat(gestoreList).hasSize(databaseSizeBeforeUpdate);
        Gestore testGestore = gestoreList.get(gestoreList.size() - 1);
        assertThat(testGestore.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testGestore.getComune()).isEqualTo(UPDATED_COMUNE);
        assertThat(testGestore.getIndirizzo()).isEqualTo(UPDATED_INDIRIZZO);
        assertThat(testGestore.getLongitudine()).isEqualTo(UPDATED_LONGITUDINE);
        assertThat(testGestore.getLatitudine()).isEqualTo(UPDATED_LATITUDINE);
        assertThat(testGestore.getMarchio()).isEqualTo(UPDATED_MARCHIO);
    }

    @Test
    @Transactional
    void updateNonExistingGestore() throws Exception {
        int databaseSizeBeforeUpdate = gestoreRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGestoreMockMvc
            .perform(put("/api/gestores").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gestore)))
            .andExpect(status().isBadRequest());

        // Validate the Gestore in the database
        List<Gestore> gestoreList = gestoreRepository.findAll();
        assertThat(gestoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGestoreWithPatch() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        int databaseSizeBeforeUpdate = gestoreRepository.findAll().size();

        // Update the gestore using partial update
        Gestore partialUpdatedGestore = new Gestore();
        partialUpdatedGestore.setId(gestore.getId());

        partialUpdatedGestore.indirizzo(UPDATED_INDIRIZZO);

        restGestoreMockMvc
            .perform(
                patch("/api/gestores")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestore))
            )
            .andExpect(status().isOk());

        // Validate the Gestore in the database
        List<Gestore> gestoreList = gestoreRepository.findAll();
        assertThat(gestoreList).hasSize(databaseSizeBeforeUpdate);
        Gestore testGestore = gestoreList.get(gestoreList.size() - 1);
        assertThat(testGestore.getProvincia()).isEqualTo(DEFAULT_PROVINCIA);
        assertThat(testGestore.getComune()).isEqualTo(DEFAULT_COMUNE);
        assertThat(testGestore.getIndirizzo()).isEqualTo(UPDATED_INDIRIZZO);
        assertThat(testGestore.getLongitudine()).isEqualTo(DEFAULT_LONGITUDINE);
        assertThat(testGestore.getLatitudine()).isEqualTo(DEFAULT_LATITUDINE);
        assertThat(testGestore.getMarchio()).isEqualTo(DEFAULT_MARCHIO);
    }

    @Test
    @Transactional
    void fullUpdateGestoreWithPatch() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        int databaseSizeBeforeUpdate = gestoreRepository.findAll().size();

        // Update the gestore using partial update
        Gestore partialUpdatedGestore = new Gestore();
        partialUpdatedGestore.setId(gestore.getId());

        partialUpdatedGestore
            .provincia(UPDATED_PROVINCIA)
            .comune(UPDATED_COMUNE)
            .indirizzo(UPDATED_INDIRIZZO)
            .longitudine(UPDATED_LONGITUDINE)
            .latitudine(UPDATED_LATITUDINE)
            .marchio(UPDATED_MARCHIO);

        restGestoreMockMvc
            .perform(
                patch("/api/gestores")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestore))
            )
            .andExpect(status().isOk());

        // Validate the Gestore in the database
        List<Gestore> gestoreList = gestoreRepository.findAll();
        assertThat(gestoreList).hasSize(databaseSizeBeforeUpdate);
        Gestore testGestore = gestoreList.get(gestoreList.size() - 1);
        assertThat(testGestore.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testGestore.getComune()).isEqualTo(UPDATED_COMUNE);
        assertThat(testGestore.getIndirizzo()).isEqualTo(UPDATED_INDIRIZZO);
        assertThat(testGestore.getLongitudine()).isEqualTo(UPDATED_LONGITUDINE);
        assertThat(testGestore.getLatitudine()).isEqualTo(UPDATED_LATITUDINE);
        assertThat(testGestore.getMarchio()).isEqualTo(UPDATED_MARCHIO);
    }

    @Test
    @Transactional
    void partialUpdateGestoreShouldThrown() throws Exception {
        // Update the gestore without id should throw
        Gestore partialUpdatedGestore = new Gestore();

        restGestoreMockMvc
            .perform(
                patch("/api/gestores")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestore))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteGestore() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        int databaseSizeBeforeDelete = gestoreRepository.findAll().size();

        // Delete the gestore
        restGestoreMockMvc
            .perform(delete("/api/gestores/{id}", gestore.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gestore> gestoreList = gestoreRepository.findAll();
        assertThat(gestoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
