package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import it.insiel.innovazione.poc.benzapp.repository.CittadinoRepository;
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
 * Integration tests for the {@link CittadinoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CittadinoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COGNOME = "AAAAAAAAAA";
    private static final String UPDATED_COGNOME = "BBBBBBBBBB";

    private static final String DEFAULT_CODICE_FISCALE = "AAAAAAAAAA";
    private static final String UPDATED_CODICE_FISCALE = "BBBBBBBBBB";

    @Autowired
    private CittadinoRepository cittadinoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCittadinoMockMvc;

    private Cittadino cittadino;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cittadino createEntity(EntityManager em) {
        Cittadino cittadino = new Cittadino().nome(DEFAULT_NOME).cognome(DEFAULT_COGNOME).codiceFiscale(DEFAULT_CODICE_FISCALE);
        return cittadino;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cittadino createUpdatedEntity(EntityManager em) {
        Cittadino cittadino = new Cittadino().nome(UPDATED_NOME).cognome(UPDATED_COGNOME).codiceFiscale(UPDATED_CODICE_FISCALE);
        return cittadino;
    }

    @BeforeEach
    public void initTest() {
        cittadino = createEntity(em);
    }

    @Test
    @Transactional
    void createCittadino() throws Exception {
        int databaseSizeBeforeCreate = cittadinoRepository.findAll().size();
        // Create the Cittadino
        restCittadinoMockMvc
            .perform(post("/api/cittadinos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cittadino)))
            .andExpect(status().isCreated());

        // Validate the Cittadino in the database
        List<Cittadino> cittadinoList = cittadinoRepository.findAll();
        assertThat(cittadinoList).hasSize(databaseSizeBeforeCreate + 1);
        Cittadino testCittadino = cittadinoList.get(cittadinoList.size() - 1);
        assertThat(testCittadino.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCittadino.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testCittadino.getCodiceFiscale()).isEqualTo(DEFAULT_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void createCittadinoWithExistingId() throws Exception {
        // Create the Cittadino with an existing ID
        cittadino.setId(1L);

        int databaseSizeBeforeCreate = cittadinoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCittadinoMockMvc
            .perform(post("/api/cittadinos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cittadino)))
            .andExpect(status().isBadRequest());

        // Validate the Cittadino in the database
        List<Cittadino> cittadinoList = cittadinoRepository.findAll();
        assertThat(cittadinoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCittadinos() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList
        restCittadinoMockMvc
            .perform(get("/api/cittadinos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cittadino.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)))
            .andExpect(jsonPath("$.[*].codiceFiscale").value(hasItem(DEFAULT_CODICE_FISCALE)));
    }

    @Test
    @Transactional
    void getCittadino() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get the cittadino
        restCittadinoMockMvc
            .perform(get("/api/cittadinos/{id}", cittadino.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cittadino.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cognome").value(DEFAULT_COGNOME))
            .andExpect(jsonPath("$.codiceFiscale").value(DEFAULT_CODICE_FISCALE));
    }

    @Test
    @Transactional
    void getNonExistingCittadino() throws Exception {
        // Get the cittadino
        restCittadinoMockMvc.perform(get("/api/cittadinos/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateCittadino() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        int databaseSizeBeforeUpdate = cittadinoRepository.findAll().size();

        // Update the cittadino
        Cittadino updatedCittadino = cittadinoRepository.findById(cittadino.getId()).get();
        // Disconnect from session so that the updates on updatedCittadino are not directly saved in db
        em.detach(updatedCittadino);
        updatedCittadino.nome(UPDATED_NOME).cognome(UPDATED_COGNOME).codiceFiscale(UPDATED_CODICE_FISCALE);

        restCittadinoMockMvc
            .perform(
                put("/api/cittadinos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedCittadino))
            )
            .andExpect(status().isOk());

        // Validate the Cittadino in the database
        List<Cittadino> cittadinoList = cittadinoRepository.findAll();
        assertThat(cittadinoList).hasSize(databaseSizeBeforeUpdate);
        Cittadino testCittadino = cittadinoList.get(cittadinoList.size() - 1);
        assertThat(testCittadino.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCittadino.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testCittadino.getCodiceFiscale()).isEqualTo(UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void updateNonExistingCittadino() throws Exception {
        int databaseSizeBeforeUpdate = cittadinoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCittadinoMockMvc
            .perform(put("/api/cittadinos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cittadino)))
            .andExpect(status().isBadRequest());

        // Validate the Cittadino in the database
        List<Cittadino> cittadinoList = cittadinoRepository.findAll();
        assertThat(cittadinoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCittadinoWithPatch() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        int databaseSizeBeforeUpdate = cittadinoRepository.findAll().size();

        // Update the cittadino using partial update
        Cittadino partialUpdatedCittadino = new Cittadino();
        partialUpdatedCittadino.setId(cittadino.getId());

        partialUpdatedCittadino.nome(UPDATED_NOME).codiceFiscale(UPDATED_CODICE_FISCALE);

        restCittadinoMockMvc
            .perform(
                patch("/api/cittadinos")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCittadino))
            )
            .andExpect(status().isOk());

        // Validate the Cittadino in the database
        List<Cittadino> cittadinoList = cittadinoRepository.findAll();
        assertThat(cittadinoList).hasSize(databaseSizeBeforeUpdate);
        Cittadino testCittadino = cittadinoList.get(cittadinoList.size() - 1);
        assertThat(testCittadino.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCittadino.getCognome()).isEqualTo(DEFAULT_COGNOME);
        assertThat(testCittadino.getCodiceFiscale()).isEqualTo(UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void fullUpdateCittadinoWithPatch() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        int databaseSizeBeforeUpdate = cittadinoRepository.findAll().size();

        // Update the cittadino using partial update
        Cittadino partialUpdatedCittadino = new Cittadino();
        partialUpdatedCittadino.setId(cittadino.getId());

        partialUpdatedCittadino.nome(UPDATED_NOME).cognome(UPDATED_COGNOME).codiceFiscale(UPDATED_CODICE_FISCALE);

        restCittadinoMockMvc
            .perform(
                patch("/api/cittadinos")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCittadino))
            )
            .andExpect(status().isOk());

        // Validate the Cittadino in the database
        List<Cittadino> cittadinoList = cittadinoRepository.findAll();
        assertThat(cittadinoList).hasSize(databaseSizeBeforeUpdate);
        Cittadino testCittadino = cittadinoList.get(cittadinoList.size() - 1);
        assertThat(testCittadino.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCittadino.getCognome()).isEqualTo(UPDATED_COGNOME);
        assertThat(testCittadino.getCodiceFiscale()).isEqualTo(UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void partialUpdateCittadinoShouldThrown() throws Exception {
        // Update the cittadino without id should throw
        Cittadino partialUpdatedCittadino = new Cittadino();

        restCittadinoMockMvc
            .perform(
                patch("/api/cittadinos")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCittadino))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteCittadino() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        int databaseSizeBeforeDelete = cittadinoRepository.findAll().size();

        // Delete the cittadino
        restCittadinoMockMvc
            .perform(delete("/api/cittadinos/{id}", cittadino.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cittadino> cittadinoList = cittadinoRepository.findAll();
        assertThat(cittadinoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
