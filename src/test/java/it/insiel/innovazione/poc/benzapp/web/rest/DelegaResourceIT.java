package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Delega;
import it.insiel.innovazione.poc.benzapp.repository.DelegaRepository;
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
 * Integration tests for the {@link DelegaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DelegaResourceIT {

    @Autowired
    private DelegaRepository delegaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDelegaMockMvc;

    private Delega delega;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delega createEntity(EntityManager em) {
        Delega delega = new Delega();
        return delega;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delega createUpdatedEntity(EntityManager em) {
        Delega delega = new Delega();
        return delega;
    }

    @BeforeEach
    public void initTest() {
        delega = createEntity(em);
    }

    @Test
    @Transactional
    void createDelega() throws Exception {
        int databaseSizeBeforeCreate = delegaRepository.findAll().size();
        // Create the Delega
        restDelegaMockMvc
            .perform(post("/api/delegas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delega)))
            .andExpect(status().isCreated());

        // Validate the Delega in the database
        List<Delega> delegaList = delegaRepository.findAll();
        assertThat(delegaList).hasSize(databaseSizeBeforeCreate + 1);
        Delega testDelega = delegaList.get(delegaList.size() - 1);
    }

    @Test
    @Transactional
    void createDelegaWithExistingId() throws Exception {
        // Create the Delega with an existing ID
        delega.setId(1L);

        int databaseSizeBeforeCreate = delegaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDelegaMockMvc
            .perform(post("/api/delegas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delega)))
            .andExpect(status().isBadRequest());

        // Validate the Delega in the database
        List<Delega> delegaList = delegaRepository.findAll();
        assertThat(delegaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDelegas() throws Exception {
        // Initialize the database
        delegaRepository.saveAndFlush(delega);

        // Get all the delegaList
        restDelegaMockMvc
            .perform(get("/api/delegas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delega.getId().intValue())));
    }

    @Test
    @Transactional
    void getDelega() throws Exception {
        // Initialize the database
        delegaRepository.saveAndFlush(delega);

        // Get the delega
        restDelegaMockMvc
            .perform(get("/api/delegas/{id}", delega.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(delega.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDelega() throws Exception {
        // Get the delega
        restDelegaMockMvc.perform(get("/api/delegas/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateDelega() throws Exception {
        // Initialize the database
        delegaRepository.saveAndFlush(delega);

        int databaseSizeBeforeUpdate = delegaRepository.findAll().size();

        // Update the delega
        Delega updatedDelega = delegaRepository.findById(delega.getId()).get();
        // Disconnect from session so that the updates on updatedDelega are not directly saved in db
        em.detach(updatedDelega);

        restDelegaMockMvc
            .perform(put("/api/delegas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedDelega)))
            .andExpect(status().isOk());

        // Validate the Delega in the database
        List<Delega> delegaList = delegaRepository.findAll();
        assertThat(delegaList).hasSize(databaseSizeBeforeUpdate);
        Delega testDelega = delegaList.get(delegaList.size() - 1);
    }

    @Test
    @Transactional
    void updateNonExistingDelega() throws Exception {
        int databaseSizeBeforeUpdate = delegaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelegaMockMvc
            .perform(put("/api/delegas").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(delega)))
            .andExpect(status().isBadRequest());

        // Validate the Delega in the database
        List<Delega> delegaList = delegaRepository.findAll();
        assertThat(delegaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDelegaWithPatch() throws Exception {
        // Initialize the database
        delegaRepository.saveAndFlush(delega);

        int databaseSizeBeforeUpdate = delegaRepository.findAll().size();

        // Update the delega using partial update
        Delega partialUpdatedDelega = new Delega();
        partialUpdatedDelega.setId(delega.getId());

        restDelegaMockMvc
            .perform(
                patch("/api/delegas")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDelega))
            )
            .andExpect(status().isOk());

        // Validate the Delega in the database
        List<Delega> delegaList = delegaRepository.findAll();
        assertThat(delegaList).hasSize(databaseSizeBeforeUpdate);
        Delega testDelega = delegaList.get(delegaList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateDelegaWithPatch() throws Exception {
        // Initialize the database
        delegaRepository.saveAndFlush(delega);

        int databaseSizeBeforeUpdate = delegaRepository.findAll().size();

        // Update the delega using partial update
        Delega partialUpdatedDelega = new Delega();
        partialUpdatedDelega.setId(delega.getId());

        restDelegaMockMvc
            .perform(
                patch("/api/delegas")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDelega))
            )
            .andExpect(status().isOk());

        // Validate the Delega in the database
        List<Delega> delegaList = delegaRepository.findAll();
        assertThat(delegaList).hasSize(databaseSizeBeforeUpdate);
        Delega testDelega = delegaList.get(delegaList.size() - 1);
    }

    @Test
    @Transactional
    void partialUpdateDelegaShouldThrown() throws Exception {
        // Update the delega without id should throw
        Delega partialUpdatedDelega = new Delega();

        restDelegaMockMvc
            .perform(
                patch("/api/delegas")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDelega))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteDelega() throws Exception {
        // Initialize the database
        delegaRepository.saveAndFlush(delega);

        int databaseSizeBeforeDelete = delegaRepository.findAll().size();

        // Delete the delega
        restDelegaMockMvc
            .perform(delete("/api/delegas/{id}", delega.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Delega> delegaList = delegaRepository.findAll();
        assertThat(delegaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
