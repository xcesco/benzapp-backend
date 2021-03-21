package it.insiel.innovazione.poc.benzapp.web.rest;

import static it.insiel.innovazione.poc.benzapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import it.insiel.innovazione.poc.benzapp.domain.Delega;
import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoVeicolo;
import it.insiel.innovazione.poc.benzapp.repository.TesseraRepository;
import it.insiel.innovazione.poc.benzapp.service.TesseraQueryService;
import it.insiel.innovazione.poc.benzapp.service.dto.TesseraCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TesseraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TesseraResourceIT {

    private static final String DEFAULT_CODICE = "AAAAAAAAAA";
    private static final String UPDATED_CODICE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_EMISSIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_EMISSIONE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_EMISSIONE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final byte[] DEFAULT_IMMAGINE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMMAGINE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMMAGINE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMMAGINE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TARGA = "AAAAAAAAAA";
    private static final String UPDATED_TARGA = "BBBBBBBBBB";

    private static final TipoVeicolo DEFAULT_VEICOLO = TipoVeicolo.CICLOMOTORE;
    private static final TipoVeicolo UPDATED_VEICOLO = TipoVeicolo.MOTOVEICOLO;

    private static final TipoCarburante DEFAULT_CARBURANTE = TipoCarburante.BENZINA;
    private static final TipoCarburante UPDATED_CARBURANTE = TipoCarburante.DIESEL;

    @Autowired
    private TesseraRepository tesseraRepository;

    @Autowired
    private TesseraQueryService tesseraQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTesseraMockMvc;

    private Tessera tessera;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tessera createEntity(EntityManager em) {
        Tessera tessera = new Tessera()
            .codice(DEFAULT_CODICE)
            .dataEmissione(DEFAULT_DATA_EMISSIONE)
            .immagine(DEFAULT_IMMAGINE)
            .immagineContentType(DEFAULT_IMMAGINE_CONTENT_TYPE)
            .targa(DEFAULT_TARGA)
            .veicolo(DEFAULT_VEICOLO)
            .carburante(DEFAULT_CARBURANTE);
        return tessera;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tessera createUpdatedEntity(EntityManager em) {
        Tessera tessera = new Tessera()
            .codice(UPDATED_CODICE)
            .dataEmissione(UPDATED_DATA_EMISSIONE)
            .immagine(UPDATED_IMMAGINE)
            .immagineContentType(UPDATED_IMMAGINE_CONTENT_TYPE)
            .targa(UPDATED_TARGA)
            .veicolo(UPDATED_VEICOLO)
            .carburante(UPDATED_CARBURANTE);
        return tessera;
    }

    @BeforeEach
    public void initTest() {
        tessera = createEntity(em);
    }

    @Test
    @Transactional
    void createTessera() throws Exception {
        int databaseSizeBeforeCreate = tesseraRepository.findAll().size();
        // Create the Tessera
        restTesseraMockMvc
            .perform(post("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isCreated());

        // Validate the Tessera in the database
        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeCreate + 1);
        Tessera testTessera = tesseraList.get(tesseraList.size() - 1);
        assertThat(testTessera.getCodice()).isEqualTo(DEFAULT_CODICE);
        assertThat(testTessera.getDataEmissione()).isEqualTo(DEFAULT_DATA_EMISSIONE);
        assertThat(testTessera.getImmagine()).isEqualTo(DEFAULT_IMMAGINE);
        assertThat(testTessera.getImmagineContentType()).isEqualTo(DEFAULT_IMMAGINE_CONTENT_TYPE);
        assertThat(testTessera.getTarga()).isEqualTo(DEFAULT_TARGA);
        assertThat(testTessera.getVeicolo()).isEqualTo(DEFAULT_VEICOLO);
        assertThat(testTessera.getCarburante()).isEqualTo(DEFAULT_CARBURANTE);
    }

    @Test
    @Transactional
    void createTesseraWithExistingId() throws Exception {
        // Create the Tessera with an existing ID
        tessera.setId(1L);

        int databaseSizeBeforeCreate = tesseraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTesseraMockMvc
            .perform(post("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isBadRequest());

        // Validate the Tessera in the database
        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = tesseraRepository.findAll().size();
        // set the field null
        tessera.setCodice(null);

        // Create the Tessera, which fails.

        restTesseraMockMvc
            .perform(post("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isBadRequest());

        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataEmissioneIsRequired() throws Exception {
        int databaseSizeBeforeTest = tesseraRepository.findAll().size();
        // set the field null
        tessera.setDataEmissione(null);

        // Create the Tessera, which fails.

        restTesseraMockMvc
            .perform(post("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isBadRequest());

        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTargaIsRequired() throws Exception {
        int databaseSizeBeforeTest = tesseraRepository.findAll().size();
        // set the field null
        tessera.setTarga(null);

        // Create the Tessera, which fails.

        restTesseraMockMvc
            .perform(post("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isBadRequest());

        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVeicoloIsRequired() throws Exception {
        int databaseSizeBeforeTest = tesseraRepository.findAll().size();
        // set the field null
        tessera.setVeicolo(null);

        // Create the Tessera, which fails.

        restTesseraMockMvc
            .perform(post("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isBadRequest());

        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCarburanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = tesseraRepository.findAll().size();
        // set the field null
        tessera.setCarburante(null);

        // Create the Tessera, which fails.

        restTesseraMockMvc
            .perform(post("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isBadRequest());

        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTesseras() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList
        restTesseraMockMvc
            .perform(get("/api/tesseras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tessera.getId().intValue())))
            .andExpect(jsonPath("$.[*].codice").value(hasItem(DEFAULT_CODICE)))
            .andExpect(jsonPath("$.[*].dataEmissione").value(hasItem(sameInstant(DEFAULT_DATA_EMISSIONE))))
            .andExpect(jsonPath("$.[*].immagineContentType").value(hasItem(DEFAULT_IMMAGINE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].immagine").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMMAGINE))))
            .andExpect(jsonPath("$.[*].targa").value(hasItem(DEFAULT_TARGA)))
            .andExpect(jsonPath("$.[*].veicolo").value(hasItem(DEFAULT_VEICOLO.toString())))
            .andExpect(jsonPath("$.[*].carburante").value(hasItem(DEFAULT_CARBURANTE.toString())));
    }

    @Test
    @Transactional
    void getTessera() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get the tessera
        restTesseraMockMvc
            .perform(get("/api/tesseras/{id}", tessera.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tessera.getId().intValue()))
            .andExpect(jsonPath("$.codice").value(DEFAULT_CODICE))
            .andExpect(jsonPath("$.dataEmissione").value(sameInstant(DEFAULT_DATA_EMISSIONE)))
            .andExpect(jsonPath("$.immagineContentType").value(DEFAULT_IMMAGINE_CONTENT_TYPE))
            .andExpect(jsonPath("$.immagine").value(Base64Utils.encodeToString(DEFAULT_IMMAGINE)))
            .andExpect(jsonPath("$.targa").value(DEFAULT_TARGA))
            .andExpect(jsonPath("$.veicolo").value(DEFAULT_VEICOLO.toString()))
            .andExpect(jsonPath("$.carburante").value(DEFAULT_CARBURANTE.toString()));
    }

    @Test
    @Transactional
    void getTesserasByIdFiltering() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        Long id = tessera.getId();

        defaultTesseraShouldBeFound("id.equals=" + id);
        defaultTesseraShouldNotBeFound("id.notEquals=" + id);

        defaultTesseraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTesseraShouldNotBeFound("id.greaterThan=" + id);

        defaultTesseraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTesseraShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTesserasByCodiceIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where codice equals to DEFAULT_CODICE
        defaultTesseraShouldBeFound("codice.equals=" + DEFAULT_CODICE);

        // Get all the tesseraList where codice equals to UPDATED_CODICE
        defaultTesseraShouldNotBeFound("codice.equals=" + UPDATED_CODICE);
    }

    @Test
    @Transactional
    void getAllTesserasByCodiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where codice not equals to DEFAULT_CODICE
        defaultTesseraShouldNotBeFound("codice.notEquals=" + DEFAULT_CODICE);

        // Get all the tesseraList where codice not equals to UPDATED_CODICE
        defaultTesseraShouldBeFound("codice.notEquals=" + UPDATED_CODICE);
    }

    @Test
    @Transactional
    void getAllTesserasByCodiceIsInShouldWork() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where codice in DEFAULT_CODICE or UPDATED_CODICE
        defaultTesseraShouldBeFound("codice.in=" + DEFAULT_CODICE + "," + UPDATED_CODICE);

        // Get all the tesseraList where codice equals to UPDATED_CODICE
        defaultTesseraShouldNotBeFound("codice.in=" + UPDATED_CODICE);
    }

    @Test
    @Transactional
    void getAllTesserasByCodiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where codice is not null
        defaultTesseraShouldBeFound("codice.specified=true");

        // Get all the tesseraList where codice is null
        defaultTesseraShouldNotBeFound("codice.specified=false");
    }

    @Test
    @Transactional
    void getAllTesserasByCodiceContainsSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where codice contains DEFAULT_CODICE
        defaultTesseraShouldBeFound("codice.contains=" + DEFAULT_CODICE);

        // Get all the tesseraList where codice contains UPDATED_CODICE
        defaultTesseraShouldNotBeFound("codice.contains=" + UPDATED_CODICE);
    }

    @Test
    @Transactional
    void getAllTesserasByCodiceNotContainsSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where codice does not contain DEFAULT_CODICE
        defaultTesseraShouldNotBeFound("codice.doesNotContain=" + DEFAULT_CODICE);

        // Get all the tesseraList where codice does not contain UPDATED_CODICE
        defaultTesseraShouldBeFound("codice.doesNotContain=" + UPDATED_CODICE);
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione equals to DEFAULT_DATA_EMISSIONE
        defaultTesseraShouldBeFound("dataEmissione.equals=" + DEFAULT_DATA_EMISSIONE);

        // Get all the tesseraList where dataEmissione equals to UPDATED_DATA_EMISSIONE
        defaultTesseraShouldNotBeFound("dataEmissione.equals=" + UPDATED_DATA_EMISSIONE);
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione not equals to DEFAULT_DATA_EMISSIONE
        defaultTesseraShouldNotBeFound("dataEmissione.notEquals=" + DEFAULT_DATA_EMISSIONE);

        // Get all the tesseraList where dataEmissione not equals to UPDATED_DATA_EMISSIONE
        defaultTesseraShouldBeFound("dataEmissione.notEquals=" + UPDATED_DATA_EMISSIONE);
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsInShouldWork() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione in DEFAULT_DATA_EMISSIONE or UPDATED_DATA_EMISSIONE
        defaultTesseraShouldBeFound("dataEmissione.in=" + DEFAULT_DATA_EMISSIONE + "," + UPDATED_DATA_EMISSIONE);

        // Get all the tesseraList where dataEmissione equals to UPDATED_DATA_EMISSIONE
        defaultTesseraShouldNotBeFound("dataEmissione.in=" + UPDATED_DATA_EMISSIONE);
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsNullOrNotNull() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione is not null
        defaultTesseraShouldBeFound("dataEmissione.specified=true");

        // Get all the tesseraList where dataEmissione is null
        defaultTesseraShouldNotBeFound("dataEmissione.specified=false");
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione is greater than or equal to DEFAULT_DATA_EMISSIONE
        defaultTesseraShouldBeFound("dataEmissione.greaterThanOrEqual=" + DEFAULT_DATA_EMISSIONE);

        // Get all the tesseraList where dataEmissione is greater than or equal to UPDATED_DATA_EMISSIONE
        defaultTesseraShouldNotBeFound("dataEmissione.greaterThanOrEqual=" + UPDATED_DATA_EMISSIONE);
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione is less than or equal to DEFAULT_DATA_EMISSIONE
        defaultTesseraShouldBeFound("dataEmissione.lessThanOrEqual=" + DEFAULT_DATA_EMISSIONE);

        // Get all the tesseraList where dataEmissione is less than or equal to SMALLER_DATA_EMISSIONE
        defaultTesseraShouldNotBeFound("dataEmissione.lessThanOrEqual=" + SMALLER_DATA_EMISSIONE);
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsLessThanSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione is less than DEFAULT_DATA_EMISSIONE
        defaultTesseraShouldNotBeFound("dataEmissione.lessThan=" + DEFAULT_DATA_EMISSIONE);

        // Get all the tesseraList where dataEmissione is less than UPDATED_DATA_EMISSIONE
        defaultTesseraShouldBeFound("dataEmissione.lessThan=" + UPDATED_DATA_EMISSIONE);
    }

    @Test
    @Transactional
    void getAllTesserasByDataEmissioneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where dataEmissione is greater than DEFAULT_DATA_EMISSIONE
        defaultTesseraShouldNotBeFound("dataEmissione.greaterThan=" + DEFAULT_DATA_EMISSIONE);

        // Get all the tesseraList where dataEmissione is greater than SMALLER_DATA_EMISSIONE
        defaultTesseraShouldBeFound("dataEmissione.greaterThan=" + SMALLER_DATA_EMISSIONE);
    }

    @Test
    @Transactional
    void getAllTesserasByTargaIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where targa equals to DEFAULT_TARGA
        defaultTesseraShouldBeFound("targa.equals=" + DEFAULT_TARGA);

        // Get all the tesseraList where targa equals to UPDATED_TARGA
        defaultTesseraShouldNotBeFound("targa.equals=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllTesserasByTargaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where targa not equals to DEFAULT_TARGA
        defaultTesseraShouldNotBeFound("targa.notEquals=" + DEFAULT_TARGA);

        // Get all the tesseraList where targa not equals to UPDATED_TARGA
        defaultTesseraShouldBeFound("targa.notEquals=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllTesserasByTargaIsInShouldWork() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where targa in DEFAULT_TARGA or UPDATED_TARGA
        defaultTesseraShouldBeFound("targa.in=" + DEFAULT_TARGA + "," + UPDATED_TARGA);

        // Get all the tesseraList where targa equals to UPDATED_TARGA
        defaultTesseraShouldNotBeFound("targa.in=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllTesserasByTargaIsNullOrNotNull() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where targa is not null
        defaultTesseraShouldBeFound("targa.specified=true");

        // Get all the tesseraList where targa is null
        defaultTesseraShouldNotBeFound("targa.specified=false");
    }

    @Test
    @Transactional
    void getAllTesserasByTargaContainsSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where targa contains DEFAULT_TARGA
        defaultTesseraShouldBeFound("targa.contains=" + DEFAULT_TARGA);

        // Get all the tesseraList where targa contains UPDATED_TARGA
        defaultTesseraShouldNotBeFound("targa.contains=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllTesserasByTargaNotContainsSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where targa does not contain DEFAULT_TARGA
        defaultTesseraShouldNotBeFound("targa.doesNotContain=" + DEFAULT_TARGA);

        // Get all the tesseraList where targa does not contain UPDATED_TARGA
        defaultTesseraShouldBeFound("targa.doesNotContain=" + UPDATED_TARGA);
    }

    @Test
    @Transactional
    void getAllTesserasByVeicoloIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where veicolo equals to DEFAULT_VEICOLO
        defaultTesseraShouldBeFound("veicolo.equals=" + DEFAULT_VEICOLO);

        // Get all the tesseraList where veicolo equals to UPDATED_VEICOLO
        defaultTesseraShouldNotBeFound("veicolo.equals=" + UPDATED_VEICOLO);
    }

    @Test
    @Transactional
    void getAllTesserasByVeicoloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where veicolo not equals to DEFAULT_VEICOLO
        defaultTesseraShouldNotBeFound("veicolo.notEquals=" + DEFAULT_VEICOLO);

        // Get all the tesseraList where veicolo not equals to UPDATED_VEICOLO
        defaultTesseraShouldBeFound("veicolo.notEquals=" + UPDATED_VEICOLO);
    }

    @Test
    @Transactional
    void getAllTesserasByVeicoloIsInShouldWork() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where veicolo in DEFAULT_VEICOLO or UPDATED_VEICOLO
        defaultTesseraShouldBeFound("veicolo.in=" + DEFAULT_VEICOLO + "," + UPDATED_VEICOLO);

        // Get all the tesseraList where veicolo equals to UPDATED_VEICOLO
        defaultTesseraShouldNotBeFound("veicolo.in=" + UPDATED_VEICOLO);
    }

    @Test
    @Transactional
    void getAllTesserasByVeicoloIsNullOrNotNull() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where veicolo is not null
        defaultTesseraShouldBeFound("veicolo.specified=true");

        // Get all the tesseraList where veicolo is null
        defaultTesseraShouldNotBeFound("veicolo.specified=false");
    }

    @Test
    @Transactional
    void getAllTesserasByCarburanteIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where carburante equals to DEFAULT_CARBURANTE
        defaultTesseraShouldBeFound("carburante.equals=" + DEFAULT_CARBURANTE);

        // Get all the tesseraList where carburante equals to UPDATED_CARBURANTE
        defaultTesseraShouldNotBeFound("carburante.equals=" + UPDATED_CARBURANTE);
    }

    @Test
    @Transactional
    void getAllTesserasByCarburanteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where carburante not equals to DEFAULT_CARBURANTE
        defaultTesseraShouldNotBeFound("carburante.notEquals=" + DEFAULT_CARBURANTE);

        // Get all the tesseraList where carburante not equals to UPDATED_CARBURANTE
        defaultTesseraShouldBeFound("carburante.notEquals=" + UPDATED_CARBURANTE);
    }

    @Test
    @Transactional
    void getAllTesserasByCarburanteIsInShouldWork() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where carburante in DEFAULT_CARBURANTE or UPDATED_CARBURANTE
        defaultTesseraShouldBeFound("carburante.in=" + DEFAULT_CARBURANTE + "," + UPDATED_CARBURANTE);

        // Get all the tesseraList where carburante equals to UPDATED_CARBURANTE
        defaultTesseraShouldNotBeFound("carburante.in=" + UPDATED_CARBURANTE);
    }

    @Test
    @Transactional
    void getAllTesserasByCarburanteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        // Get all the tesseraList where carburante is not null
        defaultTesseraShouldBeFound("carburante.specified=true");

        // Get all the tesseraList where carburante is null
        defaultTesseraShouldNotBeFound("carburante.specified=false");
    }

    @Test
    @Transactional
    void getAllTesserasByRifornimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);
        Rifornimento rifornimento = RifornimentoResourceIT.createEntity(em);
        em.persist(rifornimento);
        em.flush();
        tessera.addRifornimento(rifornimento);
        tesseraRepository.saveAndFlush(tessera);
        Long rifornimentoId = rifornimento.getId();

        // Get all the tesseraList where rifornimento equals to rifornimentoId
        defaultTesseraShouldBeFound("rifornimentoId.equals=" + rifornimentoId);

        // Get all the tesseraList where rifornimento equals to rifornimentoId + 1
        defaultTesseraShouldNotBeFound("rifornimentoId.equals=" + (rifornimentoId + 1));
    }

    @Test
    @Transactional
    void getAllTesserasByDelegaIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);
        Delega delega = DelegaResourceIT.createEntity(em);
        em.persist(delega);
        em.flush();
        tessera.addDelega(delega);
        tesseraRepository.saveAndFlush(tessera);
        Long delegaId = delega.getId();

        // Get all the tesseraList where delega equals to delegaId
        defaultTesseraShouldBeFound("delegaId.equals=" + delegaId);

        // Get all the tesseraList where delega equals to delegaId + 1
        defaultTesseraShouldNotBeFound("delegaId.equals=" + (delegaId + 1));
    }

    @Test
    @Transactional
    void getAllTesserasByCittadinoIsEqualToSomething() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);
        Cittadino cittadino = CittadinoResourceIT.createEntity(em);
        em.persist(cittadino);
        em.flush();
        tessera.setCittadino(cittadino);
        tesseraRepository.saveAndFlush(tessera);
        Long cittadinoId = cittadino.getId();

        // Get all the tesseraList where cittadino equals to cittadinoId
        defaultTesseraShouldBeFound("cittadinoId.equals=" + cittadinoId);

        // Get all the tesseraList where cittadino equals to cittadinoId + 1
        defaultTesseraShouldNotBeFound("cittadinoId.equals=" + (cittadinoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTesseraShouldBeFound(String filter) throws Exception {
        restTesseraMockMvc
            .perform(get("/api/tesseras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tessera.getId().intValue())))
            .andExpect(jsonPath("$.[*].codice").value(hasItem(DEFAULT_CODICE)))
            .andExpect(jsonPath("$.[*].dataEmissione").value(hasItem(sameInstant(DEFAULT_DATA_EMISSIONE))))
            .andExpect(jsonPath("$.[*].immagineContentType").value(hasItem(DEFAULT_IMMAGINE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].immagine").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMMAGINE))))
            .andExpect(jsonPath("$.[*].targa").value(hasItem(DEFAULT_TARGA)))
            .andExpect(jsonPath("$.[*].veicolo").value(hasItem(DEFAULT_VEICOLO.toString())))
            .andExpect(jsonPath("$.[*].carburante").value(hasItem(DEFAULT_CARBURANTE.toString())));

        // Check, that the count call also returns 1
        restTesseraMockMvc
            .perform(get("/api/tesseras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTesseraShouldNotBeFound(String filter) throws Exception {
        restTesseraMockMvc
            .perform(get("/api/tesseras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTesseraMockMvc
            .perform(get("/api/tesseras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTessera() throws Exception {
        // Get the tessera
        restTesseraMockMvc.perform(get("/api/tesseras/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateTessera() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        int databaseSizeBeforeUpdate = tesseraRepository.findAll().size();

        // Update the tessera
        Tessera updatedTessera = tesseraRepository.findById(tessera.getId()).get();
        // Disconnect from session so that the updates on updatedTessera are not directly saved in db
        em.detach(updatedTessera);
        updatedTessera
            .codice(UPDATED_CODICE)
            .dataEmissione(UPDATED_DATA_EMISSIONE)
            .immagine(UPDATED_IMMAGINE)
            .immagineContentType(UPDATED_IMMAGINE_CONTENT_TYPE)
            .targa(UPDATED_TARGA)
            .veicolo(UPDATED_VEICOLO)
            .carburante(UPDATED_CARBURANTE);

        restTesseraMockMvc
            .perform(
                put("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedTessera))
            )
            .andExpect(status().isOk());

        // Validate the Tessera in the database
        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeUpdate);
        Tessera testTessera = tesseraList.get(tesseraList.size() - 1);
        assertThat(testTessera.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testTessera.getDataEmissione()).isEqualTo(UPDATED_DATA_EMISSIONE);
        assertThat(testTessera.getImmagine()).isEqualTo(UPDATED_IMMAGINE);
        assertThat(testTessera.getImmagineContentType()).isEqualTo(UPDATED_IMMAGINE_CONTENT_TYPE);
        assertThat(testTessera.getTarga()).isEqualTo(UPDATED_TARGA);
        assertThat(testTessera.getVeicolo()).isEqualTo(UPDATED_VEICOLO);
        assertThat(testTessera.getCarburante()).isEqualTo(UPDATED_CARBURANTE);
    }

    @Test
    @Transactional
    void updateNonExistingTessera() throws Exception {
        int databaseSizeBeforeUpdate = tesseraRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTesseraMockMvc
            .perform(put("/api/tesseras").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tessera)))
            .andExpect(status().isBadRequest());

        // Validate the Tessera in the database
        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTesseraWithPatch() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        int databaseSizeBeforeUpdate = tesseraRepository.findAll().size();

        // Update the tessera using partial update
        Tessera partialUpdatedTessera = new Tessera();
        partialUpdatedTessera.setId(tessera.getId());

        partialUpdatedTessera
            .codice(UPDATED_CODICE)
            .dataEmissione(UPDATED_DATA_EMISSIONE)
            .immagine(UPDATED_IMMAGINE)
            .immagineContentType(UPDATED_IMMAGINE_CONTENT_TYPE)
            .carburante(UPDATED_CARBURANTE);

        restTesseraMockMvc
            .perform(
                patch("/api/tesseras")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTessera))
            )
            .andExpect(status().isOk());

        // Validate the Tessera in the database
        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeUpdate);
        Tessera testTessera = tesseraList.get(tesseraList.size() - 1);
        assertThat(testTessera.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testTessera.getDataEmissione()).isEqualTo(UPDATED_DATA_EMISSIONE);
        assertThat(testTessera.getImmagine()).isEqualTo(UPDATED_IMMAGINE);
        assertThat(testTessera.getImmagineContentType()).isEqualTo(UPDATED_IMMAGINE_CONTENT_TYPE);
        assertThat(testTessera.getTarga()).isEqualTo(DEFAULT_TARGA);
        assertThat(testTessera.getVeicolo()).isEqualTo(DEFAULT_VEICOLO);
        assertThat(testTessera.getCarburante()).isEqualTo(UPDATED_CARBURANTE);
    }

    @Test
    @Transactional
    void fullUpdateTesseraWithPatch() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        int databaseSizeBeforeUpdate = tesseraRepository.findAll().size();

        // Update the tessera using partial update
        Tessera partialUpdatedTessera = new Tessera();
        partialUpdatedTessera.setId(tessera.getId());

        partialUpdatedTessera
            .codice(UPDATED_CODICE)
            .dataEmissione(UPDATED_DATA_EMISSIONE)
            .immagine(UPDATED_IMMAGINE)
            .immagineContentType(UPDATED_IMMAGINE_CONTENT_TYPE)
            .targa(UPDATED_TARGA)
            .veicolo(UPDATED_VEICOLO)
            .carburante(UPDATED_CARBURANTE);

        restTesseraMockMvc
            .perform(
                patch("/api/tesseras")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTessera))
            )
            .andExpect(status().isOk());

        // Validate the Tessera in the database
        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeUpdate);
        Tessera testTessera = tesseraList.get(tesseraList.size() - 1);
        assertThat(testTessera.getCodice()).isEqualTo(UPDATED_CODICE);
        assertThat(testTessera.getDataEmissione()).isEqualTo(UPDATED_DATA_EMISSIONE);
        assertThat(testTessera.getImmagine()).isEqualTo(UPDATED_IMMAGINE);
        assertThat(testTessera.getImmagineContentType()).isEqualTo(UPDATED_IMMAGINE_CONTENT_TYPE);
        assertThat(testTessera.getTarga()).isEqualTo(UPDATED_TARGA);
        assertThat(testTessera.getVeicolo()).isEqualTo(UPDATED_VEICOLO);
        assertThat(testTessera.getCarburante()).isEqualTo(UPDATED_CARBURANTE);
    }

    @Test
    @Transactional
    void partialUpdateTesseraShouldThrown() throws Exception {
        // Update the tessera without id should throw
        Tessera partialUpdatedTessera = new Tessera();

        restTesseraMockMvc
            .perform(
                patch("/api/tesseras")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTessera))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteTessera() throws Exception {
        // Initialize the database
        tesseraRepository.saveAndFlush(tessera);

        int databaseSizeBeforeDelete = tesseraRepository.findAll().size();

        // Delete the tessera
        restTesseraMockMvc
            .perform(delete("/api/tesseras/{id}", tessera.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tessera> tesseraList = tesseraRepository.findAll();
        assertThat(tesseraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
