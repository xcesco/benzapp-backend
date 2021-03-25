package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import it.insiel.innovazione.poc.benzapp.domain.Delega;
import it.insiel.innovazione.poc.benzapp.domain.Fascia;
import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import it.insiel.innovazione.poc.benzapp.repository.CittadinoRepository;
import it.insiel.innovazione.poc.benzapp.service.CittadinoQueryService;
import it.insiel.innovazione.poc.benzapp.service.dto.CittadinoCriteria;
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

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    @Autowired
    private CittadinoRepository cittadinoRepository;

    @Autowired
    private CittadinoQueryService cittadinoQueryService;

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
        Cittadino cittadino = new Cittadino()
            .nome(DEFAULT_NOME)
            .cognome(DEFAULT_COGNOME)
            .codiceFiscale(DEFAULT_CODICE_FISCALE)
            .owner(DEFAULT_OWNER);
        return cittadino;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cittadino createUpdatedEntity(EntityManager em) {
        Cittadino cittadino = new Cittadino()
            .nome(UPDATED_NOME)
            .cognome(UPDATED_COGNOME)
            .codiceFiscale(UPDATED_CODICE_FISCALE)
            .owner(UPDATED_OWNER);
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
        assertThat(testCittadino.getOwner()).isEqualTo(DEFAULT_OWNER);
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
            .andExpect(jsonPath("$.[*].codiceFiscale").value(hasItem(DEFAULT_CODICE_FISCALE)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));
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
            .andExpect(jsonPath("$.codiceFiscale").value(DEFAULT_CODICE_FISCALE))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER));
    }

    @Test
    @Transactional
    void getCittadinosByIdFiltering() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        Long id = cittadino.getId();

        defaultCittadinoShouldBeFound("id.equals=" + id);
        defaultCittadinoShouldNotBeFound("id.notEquals=" + id);

        defaultCittadinoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCittadinoShouldNotBeFound("id.greaterThan=" + id);

        defaultCittadinoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCittadinoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCittadinosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where nome equals to DEFAULT_NOME
        defaultCittadinoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the cittadinoList where nome equals to UPDATED_NOME
        defaultCittadinoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByNomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where nome not equals to DEFAULT_NOME
        defaultCittadinoShouldNotBeFound("nome.notEquals=" + DEFAULT_NOME);

        // Get all the cittadinoList where nome not equals to UPDATED_NOME
        defaultCittadinoShouldBeFound("nome.notEquals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultCittadinoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the cittadinoList where nome equals to UPDATED_NOME
        defaultCittadinoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where nome is not null
        defaultCittadinoShouldBeFound("nome.specified=true");

        // Get all the cittadinoList where nome is null
        defaultCittadinoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllCittadinosByNomeContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where nome contains DEFAULT_NOME
        defaultCittadinoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the cittadinoList where nome contains UPDATED_NOME
        defaultCittadinoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where nome does not contain DEFAULT_NOME
        defaultCittadinoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the cittadinoList where nome does not contain UPDATED_NOME
        defaultCittadinoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByCognomeIsEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where cognome equals to DEFAULT_COGNOME
        defaultCittadinoShouldBeFound("cognome.equals=" + DEFAULT_COGNOME);

        // Get all the cittadinoList where cognome equals to UPDATED_COGNOME
        defaultCittadinoShouldNotBeFound("cognome.equals=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByCognomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where cognome not equals to DEFAULT_COGNOME
        defaultCittadinoShouldNotBeFound("cognome.notEquals=" + DEFAULT_COGNOME);

        // Get all the cittadinoList where cognome not equals to UPDATED_COGNOME
        defaultCittadinoShouldBeFound("cognome.notEquals=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByCognomeIsInShouldWork() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where cognome in DEFAULT_COGNOME or UPDATED_COGNOME
        defaultCittadinoShouldBeFound("cognome.in=" + DEFAULT_COGNOME + "," + UPDATED_COGNOME);

        // Get all the cittadinoList where cognome equals to UPDATED_COGNOME
        defaultCittadinoShouldNotBeFound("cognome.in=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByCognomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where cognome is not null
        defaultCittadinoShouldBeFound("cognome.specified=true");

        // Get all the cittadinoList where cognome is null
        defaultCittadinoShouldNotBeFound("cognome.specified=false");
    }

    @Test
    @Transactional
    void getAllCittadinosByCognomeContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where cognome contains DEFAULT_COGNOME
        defaultCittadinoShouldBeFound("cognome.contains=" + DEFAULT_COGNOME);

        // Get all the cittadinoList where cognome contains UPDATED_COGNOME
        defaultCittadinoShouldNotBeFound("cognome.contains=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByCognomeNotContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where cognome does not contain DEFAULT_COGNOME
        defaultCittadinoShouldNotBeFound("cognome.doesNotContain=" + DEFAULT_COGNOME);

        // Get all the cittadinoList where cognome does not contain UPDATED_COGNOME
        defaultCittadinoShouldBeFound("cognome.doesNotContain=" + UPDATED_COGNOME);
    }

    @Test
    @Transactional
    void getAllCittadinosByCodiceFiscaleIsEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where codiceFiscale equals to DEFAULT_CODICE_FISCALE
        defaultCittadinoShouldBeFound("codiceFiscale.equals=" + DEFAULT_CODICE_FISCALE);

        // Get all the cittadinoList where codiceFiscale equals to UPDATED_CODICE_FISCALE
        defaultCittadinoShouldNotBeFound("codiceFiscale.equals=" + UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void getAllCittadinosByCodiceFiscaleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where codiceFiscale not equals to DEFAULT_CODICE_FISCALE
        defaultCittadinoShouldNotBeFound("codiceFiscale.notEquals=" + DEFAULT_CODICE_FISCALE);

        // Get all the cittadinoList where codiceFiscale not equals to UPDATED_CODICE_FISCALE
        defaultCittadinoShouldBeFound("codiceFiscale.notEquals=" + UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void getAllCittadinosByCodiceFiscaleIsInShouldWork() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where codiceFiscale in DEFAULT_CODICE_FISCALE or UPDATED_CODICE_FISCALE
        defaultCittadinoShouldBeFound("codiceFiscale.in=" + DEFAULT_CODICE_FISCALE + "," + UPDATED_CODICE_FISCALE);

        // Get all the cittadinoList where codiceFiscale equals to UPDATED_CODICE_FISCALE
        defaultCittadinoShouldNotBeFound("codiceFiscale.in=" + UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void getAllCittadinosByCodiceFiscaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where codiceFiscale is not null
        defaultCittadinoShouldBeFound("codiceFiscale.specified=true");

        // Get all the cittadinoList where codiceFiscale is null
        defaultCittadinoShouldNotBeFound("codiceFiscale.specified=false");
    }

    @Test
    @Transactional
    void getAllCittadinosByCodiceFiscaleContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where codiceFiscale contains DEFAULT_CODICE_FISCALE
        defaultCittadinoShouldBeFound("codiceFiscale.contains=" + DEFAULT_CODICE_FISCALE);

        // Get all the cittadinoList where codiceFiscale contains UPDATED_CODICE_FISCALE
        defaultCittadinoShouldNotBeFound("codiceFiscale.contains=" + UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void getAllCittadinosByCodiceFiscaleNotContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where codiceFiscale does not contain DEFAULT_CODICE_FISCALE
        defaultCittadinoShouldNotBeFound("codiceFiscale.doesNotContain=" + DEFAULT_CODICE_FISCALE);

        // Get all the cittadinoList where codiceFiscale does not contain UPDATED_CODICE_FISCALE
        defaultCittadinoShouldBeFound("codiceFiscale.doesNotContain=" + UPDATED_CODICE_FISCALE);
    }

    @Test
    @Transactional
    void getAllCittadinosByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where owner equals to DEFAULT_OWNER
        defaultCittadinoShouldBeFound("owner.equals=" + DEFAULT_OWNER);

        // Get all the cittadinoList where owner equals to UPDATED_OWNER
        defaultCittadinoShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCittadinosByOwnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where owner not equals to DEFAULT_OWNER
        defaultCittadinoShouldNotBeFound("owner.notEquals=" + DEFAULT_OWNER);

        // Get all the cittadinoList where owner not equals to UPDATED_OWNER
        defaultCittadinoShouldBeFound("owner.notEquals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCittadinosByOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where owner in DEFAULT_OWNER or UPDATED_OWNER
        defaultCittadinoShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

        // Get all the cittadinoList where owner equals to UPDATED_OWNER
        defaultCittadinoShouldNotBeFound("owner.in=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCittadinosByOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where owner is not null
        defaultCittadinoShouldBeFound("owner.specified=true");

        // Get all the cittadinoList where owner is null
        defaultCittadinoShouldNotBeFound("owner.specified=false");
    }

    @Test
    @Transactional
    void getAllCittadinosByOwnerContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where owner contains DEFAULT_OWNER
        defaultCittadinoShouldBeFound("owner.contains=" + DEFAULT_OWNER);

        // Get all the cittadinoList where owner contains UPDATED_OWNER
        defaultCittadinoShouldNotBeFound("owner.contains=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCittadinosByOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);

        // Get all the cittadinoList where owner does not contain DEFAULT_OWNER
        defaultCittadinoShouldNotBeFound("owner.doesNotContain=" + DEFAULT_OWNER);

        // Get all the cittadinoList where owner does not contain UPDATED_OWNER
        defaultCittadinoShouldBeFound("owner.doesNotContain=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllCittadinosByDelegaIsEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);
        Delega delega = DelegaResourceIT.createEntity(em);
        em.persist(delega);
        em.flush();
        cittadino.addDelega(delega);
        cittadinoRepository.saveAndFlush(cittadino);
        Long delegaId = delega.getId();

        // Get all the cittadinoList where delega equals to delegaId
        defaultCittadinoShouldBeFound("delegaId.equals=" + delegaId);

        // Get all the cittadinoList where delega equals to delegaId + 1
        defaultCittadinoShouldNotBeFound("delegaId.equals=" + (delegaId + 1));
    }

    @Test
    @Transactional
    void getAllCittadinosByTesseraIsEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);
        Tessera tessera = TesseraResourceIT.createEntity(em);
        em.persist(tessera);
        em.flush();
        cittadino.addTessera(tessera);
        cittadinoRepository.saveAndFlush(cittadino);
        Long tesseraId = tessera.getId();

        // Get all the cittadinoList where tessera equals to tesseraId
        defaultCittadinoShouldBeFound("tesseraId.equals=" + tesseraId);

        // Get all the cittadinoList where tessera equals to tesseraId + 1
        defaultCittadinoShouldNotBeFound("tesseraId.equals=" + (tesseraId + 1));
    }

    @Test
    @Transactional
    void getAllCittadinosByFasciaIsEqualToSomething() throws Exception {
        // Initialize the database
        cittadinoRepository.saveAndFlush(cittadino);
        Fascia fascia = FasciaResourceIT.createEntity(em);
        em.persist(fascia);
        em.flush();
        cittadino.setFascia(fascia);
        cittadinoRepository.saveAndFlush(cittadino);
        Long fasciaId = fascia.getId();

        // Get all the cittadinoList where fascia equals to fasciaId
        defaultCittadinoShouldBeFound("fasciaId.equals=" + fasciaId);

        // Get all the cittadinoList where fascia equals to fasciaId + 1
        defaultCittadinoShouldNotBeFound("fasciaId.equals=" + (fasciaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCittadinoShouldBeFound(String filter) throws Exception {
        restCittadinoMockMvc
            .perform(get("/api/cittadinos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cittadino.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cognome").value(hasItem(DEFAULT_COGNOME)))
            .andExpect(jsonPath("$.[*].codiceFiscale").value(hasItem(DEFAULT_CODICE_FISCALE)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));

        // Check, that the count call also returns 1
        restCittadinoMockMvc
            .perform(get("/api/cittadinos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCittadinoShouldNotBeFound(String filter) throws Exception {
        restCittadinoMockMvc
            .perform(get("/api/cittadinos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCittadinoMockMvc
            .perform(get("/api/cittadinos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        updatedCittadino.nome(UPDATED_NOME).cognome(UPDATED_COGNOME).codiceFiscale(UPDATED_CODICE_FISCALE).owner(UPDATED_OWNER);

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
        assertThat(testCittadino.getOwner()).isEqualTo(UPDATED_OWNER);
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
        assertThat(testCittadino.getOwner()).isEqualTo(DEFAULT_OWNER);
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

        partialUpdatedCittadino.nome(UPDATED_NOME).cognome(UPDATED_COGNOME).codiceFiscale(UPDATED_CODICE_FISCALE).owner(UPDATED_OWNER);

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
        assertThat(testCittadino.getOwner()).isEqualTo(UPDATED_OWNER);
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
