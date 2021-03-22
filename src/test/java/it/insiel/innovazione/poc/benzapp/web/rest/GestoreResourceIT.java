package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Gestore;
import it.insiel.innovazione.poc.benzapp.domain.Marchio;
import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoImpianto;
import it.insiel.innovazione.poc.benzapp.repository.GestoreRepository;
import it.insiel.innovazione.poc.benzapp.service.GestoreQueryService;
import it.insiel.innovazione.poc.benzapp.service.dto.GestoreCriteria;
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
    private static final Float SMALLER_LONGITUDINE = 1F - 1F;

    private static final Float DEFAULT_LATITUDINE = 1F;
    private static final Float UPDATED_LATITUDINE = 2F;
    private static final Float SMALLER_LATITUDINE = 1F - 1F;

    private static final TipoImpianto DEFAULT_TIPO = TipoImpianto.AUTOSTRADALE;
    private static final TipoImpianto UPDATED_TIPO = TipoImpianto.STRADALE;

    private static final Float DEFAULT_BENZINA_PREZZO_AL_LITRO = 1F;
    private static final Float UPDATED_BENZINA_PREZZO_AL_LITRO = 2F;
    private static final Float SMALLER_BENZINA_PREZZO_AL_LITRO = 1F - 1F;

    private static final Float DEFAULT_GASOLIO_PREZZO_AL_LITRO = 1F;
    private static final Float UPDATED_GASOLIO_PREZZO_AL_LITRO = 2F;
    private static final Float SMALLER_GASOLIO_PREZZO_AL_LITRO = 1F - 1F;

    @Autowired
    private GestoreRepository gestoreRepository;

    @Autowired
    private GestoreQueryService gestoreQueryService;

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
            .tipo(DEFAULT_TIPO)
            .benzinaPrezzoAlLitro(DEFAULT_BENZINA_PREZZO_AL_LITRO)
            .gasolioPrezzoAlLitro(DEFAULT_GASOLIO_PREZZO_AL_LITRO);
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
            .tipo(UPDATED_TIPO)
            .benzinaPrezzoAlLitro(UPDATED_BENZINA_PREZZO_AL_LITRO)
            .gasolioPrezzoAlLitro(UPDATED_GASOLIO_PREZZO_AL_LITRO);
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
        assertThat(testGestore.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testGestore.getBenzinaPrezzoAlLitro()).isEqualTo(DEFAULT_BENZINA_PREZZO_AL_LITRO);
        assertThat(testGestore.getGasolioPrezzoAlLitro()).isEqualTo(DEFAULT_GASOLIO_PREZZO_AL_LITRO);
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
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].benzinaPrezzoAlLitro").value(hasItem(DEFAULT_BENZINA_PREZZO_AL_LITRO.doubleValue())))
            .andExpect(jsonPath("$.[*].gasolioPrezzoAlLitro").value(hasItem(DEFAULT_GASOLIO_PREZZO_AL_LITRO.doubleValue())));
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
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.benzinaPrezzoAlLitro").value(DEFAULT_BENZINA_PREZZO_AL_LITRO.doubleValue()))
            .andExpect(jsonPath("$.gasolioPrezzoAlLitro").value(DEFAULT_GASOLIO_PREZZO_AL_LITRO.doubleValue()));
    }

    @Test
    @Transactional
    void getGestoresByIdFiltering() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        Long id = gestore.getId();

        defaultGestoreShouldBeFound("id.equals=" + id);
        defaultGestoreShouldNotBeFound("id.notEquals=" + id);

        defaultGestoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGestoreShouldNotBeFound("id.greaterThan=" + id);

        defaultGestoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGestoreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGestoresByProvinciaIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where provincia equals to DEFAULT_PROVINCIA
        defaultGestoreShouldBeFound("provincia.equals=" + DEFAULT_PROVINCIA);

        // Get all the gestoreList where provincia equals to UPDATED_PROVINCIA
        defaultGestoreShouldNotBeFound("provincia.equals=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllGestoresByProvinciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where provincia not equals to DEFAULT_PROVINCIA
        defaultGestoreShouldNotBeFound("provincia.notEquals=" + DEFAULT_PROVINCIA);

        // Get all the gestoreList where provincia not equals to UPDATED_PROVINCIA
        defaultGestoreShouldBeFound("provincia.notEquals=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllGestoresByProvinciaIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where provincia in DEFAULT_PROVINCIA or UPDATED_PROVINCIA
        defaultGestoreShouldBeFound("provincia.in=" + DEFAULT_PROVINCIA + "," + UPDATED_PROVINCIA);

        // Get all the gestoreList where provincia equals to UPDATED_PROVINCIA
        defaultGestoreShouldNotBeFound("provincia.in=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllGestoresByProvinciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where provincia is not null
        defaultGestoreShouldBeFound("provincia.specified=true");

        // Get all the gestoreList where provincia is null
        defaultGestoreShouldNotBeFound("provincia.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByProvinciaContainsSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where provincia contains DEFAULT_PROVINCIA
        defaultGestoreShouldBeFound("provincia.contains=" + DEFAULT_PROVINCIA);

        // Get all the gestoreList where provincia contains UPDATED_PROVINCIA
        defaultGestoreShouldNotBeFound("provincia.contains=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllGestoresByProvinciaNotContainsSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where provincia does not contain DEFAULT_PROVINCIA
        defaultGestoreShouldNotBeFound("provincia.doesNotContain=" + DEFAULT_PROVINCIA);

        // Get all the gestoreList where provincia does not contain UPDATED_PROVINCIA
        defaultGestoreShouldBeFound("provincia.doesNotContain=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllGestoresByComuneIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where comune equals to DEFAULT_COMUNE
        defaultGestoreShouldBeFound("comune.equals=" + DEFAULT_COMUNE);

        // Get all the gestoreList where comune equals to UPDATED_COMUNE
        defaultGestoreShouldNotBeFound("comune.equals=" + UPDATED_COMUNE);
    }

    @Test
    @Transactional
    void getAllGestoresByComuneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where comune not equals to DEFAULT_COMUNE
        defaultGestoreShouldNotBeFound("comune.notEquals=" + DEFAULT_COMUNE);

        // Get all the gestoreList where comune not equals to UPDATED_COMUNE
        defaultGestoreShouldBeFound("comune.notEquals=" + UPDATED_COMUNE);
    }

    @Test
    @Transactional
    void getAllGestoresByComuneIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where comune in DEFAULT_COMUNE or UPDATED_COMUNE
        defaultGestoreShouldBeFound("comune.in=" + DEFAULT_COMUNE + "," + UPDATED_COMUNE);

        // Get all the gestoreList where comune equals to UPDATED_COMUNE
        defaultGestoreShouldNotBeFound("comune.in=" + UPDATED_COMUNE);
    }

    @Test
    @Transactional
    void getAllGestoresByComuneIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where comune is not null
        defaultGestoreShouldBeFound("comune.specified=true");

        // Get all the gestoreList where comune is null
        defaultGestoreShouldNotBeFound("comune.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByComuneContainsSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where comune contains DEFAULT_COMUNE
        defaultGestoreShouldBeFound("comune.contains=" + DEFAULT_COMUNE);

        // Get all the gestoreList where comune contains UPDATED_COMUNE
        defaultGestoreShouldNotBeFound("comune.contains=" + UPDATED_COMUNE);
    }

    @Test
    @Transactional
    void getAllGestoresByComuneNotContainsSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where comune does not contain DEFAULT_COMUNE
        defaultGestoreShouldNotBeFound("comune.doesNotContain=" + DEFAULT_COMUNE);

        // Get all the gestoreList where comune does not contain UPDATED_COMUNE
        defaultGestoreShouldBeFound("comune.doesNotContain=" + UPDATED_COMUNE);
    }

    @Test
    @Transactional
    void getAllGestoresByIndirizzoIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where indirizzo equals to DEFAULT_INDIRIZZO
        defaultGestoreShouldBeFound("indirizzo.equals=" + DEFAULT_INDIRIZZO);

        // Get all the gestoreList where indirizzo equals to UPDATED_INDIRIZZO
        defaultGestoreShouldNotBeFound("indirizzo.equals=" + UPDATED_INDIRIZZO);
    }

    @Test
    @Transactional
    void getAllGestoresByIndirizzoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where indirizzo not equals to DEFAULT_INDIRIZZO
        defaultGestoreShouldNotBeFound("indirizzo.notEquals=" + DEFAULT_INDIRIZZO);

        // Get all the gestoreList where indirizzo not equals to UPDATED_INDIRIZZO
        defaultGestoreShouldBeFound("indirizzo.notEquals=" + UPDATED_INDIRIZZO);
    }

    @Test
    @Transactional
    void getAllGestoresByIndirizzoIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where indirizzo in DEFAULT_INDIRIZZO or UPDATED_INDIRIZZO
        defaultGestoreShouldBeFound("indirizzo.in=" + DEFAULT_INDIRIZZO + "," + UPDATED_INDIRIZZO);

        // Get all the gestoreList where indirizzo equals to UPDATED_INDIRIZZO
        defaultGestoreShouldNotBeFound("indirizzo.in=" + UPDATED_INDIRIZZO);
    }

    @Test
    @Transactional
    void getAllGestoresByIndirizzoIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where indirizzo is not null
        defaultGestoreShouldBeFound("indirizzo.specified=true");

        // Get all the gestoreList where indirizzo is null
        defaultGestoreShouldNotBeFound("indirizzo.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByIndirizzoContainsSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where indirizzo contains DEFAULT_INDIRIZZO
        defaultGestoreShouldBeFound("indirizzo.contains=" + DEFAULT_INDIRIZZO);

        // Get all the gestoreList where indirizzo contains UPDATED_INDIRIZZO
        defaultGestoreShouldNotBeFound("indirizzo.contains=" + UPDATED_INDIRIZZO);
    }

    @Test
    @Transactional
    void getAllGestoresByIndirizzoNotContainsSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where indirizzo does not contain DEFAULT_INDIRIZZO
        defaultGestoreShouldNotBeFound("indirizzo.doesNotContain=" + DEFAULT_INDIRIZZO);

        // Get all the gestoreList where indirizzo does not contain UPDATED_INDIRIZZO
        defaultGestoreShouldBeFound("indirizzo.doesNotContain=" + UPDATED_INDIRIZZO);
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine equals to DEFAULT_LONGITUDINE
        defaultGestoreShouldBeFound("longitudine.equals=" + DEFAULT_LONGITUDINE);

        // Get all the gestoreList where longitudine equals to UPDATED_LONGITUDINE
        defaultGestoreShouldNotBeFound("longitudine.equals=" + UPDATED_LONGITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine not equals to DEFAULT_LONGITUDINE
        defaultGestoreShouldNotBeFound("longitudine.notEquals=" + DEFAULT_LONGITUDINE);

        // Get all the gestoreList where longitudine not equals to UPDATED_LONGITUDINE
        defaultGestoreShouldBeFound("longitudine.notEquals=" + UPDATED_LONGITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine in DEFAULT_LONGITUDINE or UPDATED_LONGITUDINE
        defaultGestoreShouldBeFound("longitudine.in=" + DEFAULT_LONGITUDINE + "," + UPDATED_LONGITUDINE);

        // Get all the gestoreList where longitudine equals to UPDATED_LONGITUDINE
        defaultGestoreShouldNotBeFound("longitudine.in=" + UPDATED_LONGITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine is not null
        defaultGestoreShouldBeFound("longitudine.specified=true");

        // Get all the gestoreList where longitudine is null
        defaultGestoreShouldNotBeFound("longitudine.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine is greater than or equal to DEFAULT_LONGITUDINE
        defaultGestoreShouldBeFound("longitudine.greaterThanOrEqual=" + DEFAULT_LONGITUDINE);

        // Get all the gestoreList where longitudine is greater than or equal to UPDATED_LONGITUDINE
        defaultGestoreShouldNotBeFound("longitudine.greaterThanOrEqual=" + UPDATED_LONGITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine is less than or equal to DEFAULT_LONGITUDINE
        defaultGestoreShouldBeFound("longitudine.lessThanOrEqual=" + DEFAULT_LONGITUDINE);

        // Get all the gestoreList where longitudine is less than or equal to SMALLER_LONGITUDINE
        defaultGestoreShouldNotBeFound("longitudine.lessThanOrEqual=" + SMALLER_LONGITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsLessThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine is less than DEFAULT_LONGITUDINE
        defaultGestoreShouldNotBeFound("longitudine.lessThan=" + DEFAULT_LONGITUDINE);

        // Get all the gestoreList where longitudine is less than UPDATED_LONGITUDINE
        defaultGestoreShouldBeFound("longitudine.lessThan=" + UPDATED_LONGITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLongitudineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where longitudine is greater than DEFAULT_LONGITUDINE
        defaultGestoreShouldNotBeFound("longitudine.greaterThan=" + DEFAULT_LONGITUDINE);

        // Get all the gestoreList where longitudine is greater than SMALLER_LONGITUDINE
        defaultGestoreShouldBeFound("longitudine.greaterThan=" + SMALLER_LONGITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine equals to DEFAULT_LATITUDINE
        defaultGestoreShouldBeFound("latitudine.equals=" + DEFAULT_LATITUDINE);

        // Get all the gestoreList where latitudine equals to UPDATED_LATITUDINE
        defaultGestoreShouldNotBeFound("latitudine.equals=" + UPDATED_LATITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine not equals to DEFAULT_LATITUDINE
        defaultGestoreShouldNotBeFound("latitudine.notEquals=" + DEFAULT_LATITUDINE);

        // Get all the gestoreList where latitudine not equals to UPDATED_LATITUDINE
        defaultGestoreShouldBeFound("latitudine.notEquals=" + UPDATED_LATITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine in DEFAULT_LATITUDINE or UPDATED_LATITUDINE
        defaultGestoreShouldBeFound("latitudine.in=" + DEFAULT_LATITUDINE + "," + UPDATED_LATITUDINE);

        // Get all the gestoreList where latitudine equals to UPDATED_LATITUDINE
        defaultGestoreShouldNotBeFound("latitudine.in=" + UPDATED_LATITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine is not null
        defaultGestoreShouldBeFound("latitudine.specified=true");

        // Get all the gestoreList where latitudine is null
        defaultGestoreShouldNotBeFound("latitudine.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine is greater than or equal to DEFAULT_LATITUDINE
        defaultGestoreShouldBeFound("latitudine.greaterThanOrEqual=" + DEFAULT_LATITUDINE);

        // Get all the gestoreList where latitudine is greater than or equal to UPDATED_LATITUDINE
        defaultGestoreShouldNotBeFound("latitudine.greaterThanOrEqual=" + UPDATED_LATITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine is less than or equal to DEFAULT_LATITUDINE
        defaultGestoreShouldBeFound("latitudine.lessThanOrEqual=" + DEFAULT_LATITUDINE);

        // Get all the gestoreList where latitudine is less than or equal to SMALLER_LATITUDINE
        defaultGestoreShouldNotBeFound("latitudine.lessThanOrEqual=" + SMALLER_LATITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsLessThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine is less than DEFAULT_LATITUDINE
        defaultGestoreShouldNotBeFound("latitudine.lessThan=" + DEFAULT_LATITUDINE);

        // Get all the gestoreList where latitudine is less than UPDATED_LATITUDINE
        defaultGestoreShouldBeFound("latitudine.lessThan=" + UPDATED_LATITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByLatitudineIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where latitudine is greater than DEFAULT_LATITUDINE
        defaultGestoreShouldNotBeFound("latitudine.greaterThan=" + DEFAULT_LATITUDINE);

        // Get all the gestoreList where latitudine is greater than SMALLER_LATITUDINE
        defaultGestoreShouldBeFound("latitudine.greaterThan=" + SMALLER_LATITUDINE);
    }

    @Test
    @Transactional
    void getAllGestoresByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where tipo equals to DEFAULT_TIPO
        defaultGestoreShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the gestoreList where tipo equals to UPDATED_TIPO
        defaultGestoreShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllGestoresByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where tipo not equals to DEFAULT_TIPO
        defaultGestoreShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the gestoreList where tipo not equals to UPDATED_TIPO
        defaultGestoreShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllGestoresByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultGestoreShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the gestoreList where tipo equals to UPDATED_TIPO
        defaultGestoreShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllGestoresByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where tipo is not null
        defaultGestoreShouldBeFound("tipo.specified=true");

        // Get all the gestoreList where tipo is null
        defaultGestoreShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro equals to DEFAULT_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.equals=" + DEFAULT_BENZINA_PREZZO_AL_LITRO);

        // Get all the gestoreList where benzinaPrezzoAlLitro equals to UPDATED_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.equals=" + UPDATED_BENZINA_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro not equals to DEFAULT_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.notEquals=" + DEFAULT_BENZINA_PREZZO_AL_LITRO);

        // Get all the gestoreList where benzinaPrezzoAlLitro not equals to UPDATED_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.notEquals=" + UPDATED_BENZINA_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro in DEFAULT_BENZINA_PREZZO_AL_LITRO or UPDATED_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.in=" + DEFAULT_BENZINA_PREZZO_AL_LITRO + "," + UPDATED_BENZINA_PREZZO_AL_LITRO);

        // Get all the gestoreList where benzinaPrezzoAlLitro equals to UPDATED_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.in=" + UPDATED_BENZINA_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro is not null
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.specified=true");

        // Get all the gestoreList where benzinaPrezzoAlLitro is null
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro is greater than or equal to DEFAULT_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.greaterThanOrEqual=" + DEFAULT_BENZINA_PREZZO_AL_LITRO);

        // Get all the gestoreList where benzinaPrezzoAlLitro is greater than or equal to UPDATED_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.greaterThanOrEqual=" + UPDATED_BENZINA_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro is less than or equal to DEFAULT_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.lessThanOrEqual=" + DEFAULT_BENZINA_PREZZO_AL_LITRO);

        // Get all the gestoreList where benzinaPrezzoAlLitro is less than or equal to SMALLER_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.lessThanOrEqual=" + SMALLER_BENZINA_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsLessThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro is less than DEFAULT_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.lessThan=" + DEFAULT_BENZINA_PREZZO_AL_LITRO);

        // Get all the gestoreList where benzinaPrezzoAlLitro is less than UPDATED_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.lessThan=" + UPDATED_BENZINA_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByBenzinaPrezzoAlLitroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where benzinaPrezzoAlLitro is greater than DEFAULT_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("benzinaPrezzoAlLitro.greaterThan=" + DEFAULT_BENZINA_PREZZO_AL_LITRO);

        // Get all the gestoreList where benzinaPrezzoAlLitro is greater than SMALLER_BENZINA_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("benzinaPrezzoAlLitro.greaterThan=" + SMALLER_BENZINA_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro equals to DEFAULT_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.equals=" + DEFAULT_GASOLIO_PREZZO_AL_LITRO);

        // Get all the gestoreList where gasolioPrezzoAlLitro equals to UPDATED_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.equals=" + UPDATED_GASOLIO_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro not equals to DEFAULT_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.notEquals=" + DEFAULT_GASOLIO_PREZZO_AL_LITRO);

        // Get all the gestoreList where gasolioPrezzoAlLitro not equals to UPDATED_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.notEquals=" + UPDATED_GASOLIO_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsInShouldWork() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro in DEFAULT_GASOLIO_PREZZO_AL_LITRO or UPDATED_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.in=" + DEFAULT_GASOLIO_PREZZO_AL_LITRO + "," + UPDATED_GASOLIO_PREZZO_AL_LITRO);

        // Get all the gestoreList where gasolioPrezzoAlLitro equals to UPDATED_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.in=" + UPDATED_GASOLIO_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro is not null
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.specified=true");

        // Get all the gestoreList where gasolioPrezzoAlLitro is null
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.specified=false");
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro is greater than or equal to DEFAULT_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.greaterThanOrEqual=" + DEFAULT_GASOLIO_PREZZO_AL_LITRO);

        // Get all the gestoreList where gasolioPrezzoAlLitro is greater than or equal to UPDATED_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.greaterThanOrEqual=" + UPDATED_GASOLIO_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro is less than or equal to DEFAULT_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.lessThanOrEqual=" + DEFAULT_GASOLIO_PREZZO_AL_LITRO);

        // Get all the gestoreList where gasolioPrezzoAlLitro is less than or equal to SMALLER_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.lessThanOrEqual=" + SMALLER_GASOLIO_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsLessThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro is less than DEFAULT_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.lessThan=" + DEFAULT_GASOLIO_PREZZO_AL_LITRO);

        // Get all the gestoreList where gasolioPrezzoAlLitro is less than UPDATED_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.lessThan=" + UPDATED_GASOLIO_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByGasolioPrezzoAlLitroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);

        // Get all the gestoreList where gasolioPrezzoAlLitro is greater than DEFAULT_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldNotBeFound("gasolioPrezzoAlLitro.greaterThan=" + DEFAULT_GASOLIO_PREZZO_AL_LITRO);

        // Get all the gestoreList where gasolioPrezzoAlLitro is greater than SMALLER_GASOLIO_PREZZO_AL_LITRO
        defaultGestoreShouldBeFound("gasolioPrezzoAlLitro.greaterThan=" + SMALLER_GASOLIO_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllGestoresByRifornimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);
        Rifornimento rifornimento = RifornimentoResourceIT.createEntity(em);
        em.persist(rifornimento);
        em.flush();
        gestore.addRifornimento(rifornimento);
        gestoreRepository.saveAndFlush(gestore);
        Long rifornimentoId = rifornimento.getId();

        // Get all the gestoreList where rifornimento equals to rifornimentoId
        defaultGestoreShouldBeFound("rifornimentoId.equals=" + rifornimentoId);

        // Get all the gestoreList where rifornimento equals to rifornimentoId + 1
        defaultGestoreShouldNotBeFound("rifornimentoId.equals=" + (rifornimentoId + 1));
    }

    @Test
    @Transactional
    void getAllGestoresByMarchioIsEqualToSomething() throws Exception {
        // Initialize the database
        gestoreRepository.saveAndFlush(gestore);
        Marchio marchio = MarchioResourceIT.createEntity(em);
        em.persist(marchio);
        em.flush();
        gestore.setMarchio(marchio);
        gestoreRepository.saveAndFlush(gestore);
        Long marchioId = marchio.getId();

        // Get all the gestoreList where marchio equals to marchioId
        defaultGestoreShouldBeFound("marchioId.equals=" + marchioId);

        // Get all the gestoreList where marchio equals to marchioId + 1
        defaultGestoreShouldNotBeFound("marchioId.equals=" + (marchioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGestoreShouldBeFound(String filter) throws Exception {
        restGestoreMockMvc
            .perform(get("/api/gestores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestore.getId().intValue())))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)))
            .andExpect(jsonPath("$.[*].comune").value(hasItem(DEFAULT_COMUNE)))
            .andExpect(jsonPath("$.[*].indirizzo").value(hasItem(DEFAULT_INDIRIZZO)))
            .andExpect(jsonPath("$.[*].longitudine").value(hasItem(DEFAULT_LONGITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitudine").value(hasItem(DEFAULT_LATITUDINE.doubleValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].benzinaPrezzoAlLitro").value(hasItem(DEFAULT_BENZINA_PREZZO_AL_LITRO.doubleValue())))
            .andExpect(jsonPath("$.[*].gasolioPrezzoAlLitro").value(hasItem(DEFAULT_GASOLIO_PREZZO_AL_LITRO.doubleValue())));

        // Check, that the count call also returns 1
        restGestoreMockMvc
            .perform(get("/api/gestores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGestoreShouldNotBeFound(String filter) throws Exception {
        restGestoreMockMvc
            .perform(get("/api/gestores?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGestoreMockMvc
            .perform(get("/api/gestores/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .tipo(UPDATED_TIPO)
            .benzinaPrezzoAlLitro(UPDATED_BENZINA_PREZZO_AL_LITRO)
            .gasolioPrezzoAlLitro(UPDATED_GASOLIO_PREZZO_AL_LITRO);

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
        assertThat(testGestore.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testGestore.getBenzinaPrezzoAlLitro()).isEqualTo(UPDATED_BENZINA_PREZZO_AL_LITRO);
        assertThat(testGestore.getGasolioPrezzoAlLitro()).isEqualTo(UPDATED_GASOLIO_PREZZO_AL_LITRO);
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

        partialUpdatedGestore
            .indirizzo(UPDATED_INDIRIZZO)
            .benzinaPrezzoAlLitro(UPDATED_BENZINA_PREZZO_AL_LITRO)
            .gasolioPrezzoAlLitro(UPDATED_GASOLIO_PREZZO_AL_LITRO);

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
        assertThat(testGestore.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testGestore.getBenzinaPrezzoAlLitro()).isEqualTo(UPDATED_BENZINA_PREZZO_AL_LITRO);
        assertThat(testGestore.getGasolioPrezzoAlLitro()).isEqualTo(UPDATED_GASOLIO_PREZZO_AL_LITRO);
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
            .tipo(UPDATED_TIPO)
            .benzinaPrezzoAlLitro(UPDATED_BENZINA_PREZZO_AL_LITRO)
            .gasolioPrezzoAlLitro(UPDATED_GASOLIO_PREZZO_AL_LITRO);

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
        assertThat(testGestore.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testGestore.getBenzinaPrezzoAlLitro()).isEqualTo(UPDATED_BENZINA_PREZZO_AL_LITRO);
        assertThat(testGestore.getGasolioPrezzoAlLitro()).isEqualTo(UPDATED_GASOLIO_PREZZO_AL_LITRO);
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
