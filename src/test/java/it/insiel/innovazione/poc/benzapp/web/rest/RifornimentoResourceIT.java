package it.insiel.innovazione.poc.benzapp.web.rest;

import static it.insiel.innovazione.poc.benzapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Gestore;
import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
import it.insiel.innovazione.poc.benzapp.repository.RifornimentoRepository;
import it.insiel.innovazione.poc.benzapp.service.RifornimentoQueryService;
import it.insiel.innovazione.poc.benzapp.service.dto.RifornimentoCriteria;
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

/**
 * Integration tests for the {@link RifornimentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RifornimentoResourceIT {

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_PROGRESSIVO = 1;
    private static final Integer UPDATED_PROGRESSIVO = 2;
    private static final Integer SMALLER_PROGRESSIVO = 1 - 1;

    private static final Float DEFAULT_LITRI_EROGATI = 1F;
    private static final Float UPDATED_LITRI_EROGATI = 2F;
    private static final Float SMALLER_LITRI_EROGATI = 1F - 1F;

    private static final Float DEFAULT_SCONTO = 1F;
    private static final Float UPDATED_SCONTO = 2F;
    private static final Float SMALLER_SCONTO = 1F - 1F;

    private static final Float DEFAULT_PREZZO_AL_LITRO = 1F;
    private static final Float UPDATED_PREZZO_AL_LITRO = 2F;
    private static final Float SMALLER_PREZZO_AL_LITRO = 1F - 1F;

    private static final TipoCarburante DEFAULT_TIPO_CARBURANTE = TipoCarburante.BENZINA;
    private static final TipoCarburante UPDATED_TIPO_CARBURANTE = TipoCarburante.DIESEL;

    @Autowired
    private RifornimentoRepository rifornimentoRepository;

    @Autowired
    private RifornimentoQueryService rifornimentoQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRifornimentoMockMvc;

    private Rifornimento rifornimento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rifornimento createEntity(EntityManager em) {
        Rifornimento rifornimento = new Rifornimento()
            .data(DEFAULT_DATA)
            .progressivo(DEFAULT_PROGRESSIVO)
            .litriErogati(DEFAULT_LITRI_EROGATI)
            .sconto(DEFAULT_SCONTO)
            .prezzoAlLitro(DEFAULT_PREZZO_AL_LITRO)
            .tipoCarburante(DEFAULT_TIPO_CARBURANTE);
        return rifornimento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rifornimento createUpdatedEntity(EntityManager em) {
        Rifornimento rifornimento = new Rifornimento()
            .data(UPDATED_DATA)
            .progressivo(UPDATED_PROGRESSIVO)
            .litriErogati(UPDATED_LITRI_EROGATI)
            .sconto(UPDATED_SCONTO)
            .prezzoAlLitro(UPDATED_PREZZO_AL_LITRO)
            .tipoCarburante(UPDATED_TIPO_CARBURANTE);
        return rifornimento;
    }

    @BeforeEach
    public void initTest() {
        rifornimento = createEntity(em);
    }

    @Test
    @Transactional
    void createRifornimento() throws Exception {
        int databaseSizeBeforeCreate = rifornimentoRepository.findAll().size();
        // Create the Rifornimento
        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isCreated());

        // Validate the Rifornimento in the database
        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeCreate + 1);
        Rifornimento testRifornimento = rifornimentoList.get(rifornimentoList.size() - 1);
        assertThat(testRifornimento.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testRifornimento.getProgressivo()).isEqualTo(DEFAULT_PROGRESSIVO);
        assertThat(testRifornimento.getLitriErogati()).isEqualTo(DEFAULT_LITRI_EROGATI);
        assertThat(testRifornimento.getSconto()).isEqualTo(DEFAULT_SCONTO);
        assertThat(testRifornimento.getPrezzoAlLitro()).isEqualTo(DEFAULT_PREZZO_AL_LITRO);
        assertThat(testRifornimento.getTipoCarburante()).isEqualTo(DEFAULT_TIPO_CARBURANTE);
    }

    @Test
    @Transactional
    void createRifornimentoWithExistingId() throws Exception {
        // Create the Rifornimento with an existing ID
        rifornimento.setId(1L);

        int databaseSizeBeforeCreate = rifornimentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rifornimento in the database
        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = rifornimentoRepository.findAll().size();
        // set the field null
        rifornimento.setData(null);

        // Create the Rifornimento, which fails.

        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProgressivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = rifornimentoRepository.findAll().size();
        // set the field null
        rifornimento.setProgressivo(null);

        // Create the Rifornimento, which fails.

        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLitriErogatiIsRequired() throws Exception {
        int databaseSizeBeforeTest = rifornimentoRepository.findAll().size();
        // set the field null
        rifornimento.setLitriErogati(null);

        // Create the Rifornimento, which fails.

        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = rifornimentoRepository.findAll().size();
        // set the field null
        rifornimento.setSconto(null);

        // Create the Rifornimento, which fails.

        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrezzoAlLitroIsRequired() throws Exception {
        int databaseSizeBeforeTest = rifornimentoRepository.findAll().size();
        // set the field null
        rifornimento.setPrezzoAlLitro(null);

        // Create the Rifornimento, which fails.

        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoCarburanteIsRequired() throws Exception {
        int databaseSizeBeforeTest = rifornimentoRepository.findAll().size();
        // set the field null
        rifornimento.setTipoCarburante(null);

        // Create the Rifornimento, which fails.

        restRifornimentoMockMvc
            .perform(
                post("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRifornimentos() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList
        restRifornimentoMockMvc
            .perform(get("/api/rifornimentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rifornimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].progressivo").value(hasItem(DEFAULT_PROGRESSIVO)))
            .andExpect(jsonPath("$.[*].litriErogati").value(hasItem(DEFAULT_LITRI_EROGATI.doubleValue())))
            .andExpect(jsonPath("$.[*].sconto").value(hasItem(DEFAULT_SCONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].prezzoAlLitro").value(hasItem(DEFAULT_PREZZO_AL_LITRO.doubleValue())))
            .andExpect(jsonPath("$.[*].tipoCarburante").value(hasItem(DEFAULT_TIPO_CARBURANTE.toString())));
    }

    @Test
    @Transactional
    void getRifornimento() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get the rifornimento
        restRifornimentoMockMvc
            .perform(get("/api/rifornimentos/{id}", rifornimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rifornimento.getId().intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.progressivo").value(DEFAULT_PROGRESSIVO))
            .andExpect(jsonPath("$.litriErogati").value(DEFAULT_LITRI_EROGATI.doubleValue()))
            .andExpect(jsonPath("$.sconto").value(DEFAULT_SCONTO.doubleValue()))
            .andExpect(jsonPath("$.prezzoAlLitro").value(DEFAULT_PREZZO_AL_LITRO.doubleValue()))
            .andExpect(jsonPath("$.tipoCarburante").value(DEFAULT_TIPO_CARBURANTE.toString()));
    }

    @Test
    @Transactional
    void getRifornimentosByIdFiltering() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        Long id = rifornimento.getId();

        defaultRifornimentoShouldBeFound("id.equals=" + id);
        defaultRifornimentoShouldNotBeFound("id.notEquals=" + id);

        defaultRifornimentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRifornimentoShouldNotBeFound("id.greaterThan=" + id);

        defaultRifornimentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRifornimentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data equals to DEFAULT_DATA
        defaultRifornimentoShouldBeFound("data.equals=" + DEFAULT_DATA);

        // Get all the rifornimentoList where data equals to UPDATED_DATA
        defaultRifornimentoShouldNotBeFound("data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data not equals to DEFAULT_DATA
        defaultRifornimentoShouldNotBeFound("data.notEquals=" + DEFAULT_DATA);

        // Get all the rifornimentoList where data not equals to UPDATED_DATA
        defaultRifornimentoShouldBeFound("data.notEquals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsInShouldWork() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data in DEFAULT_DATA or UPDATED_DATA
        defaultRifornimentoShouldBeFound("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA);

        // Get all the rifornimentoList where data equals to UPDATED_DATA
        defaultRifornimentoShouldNotBeFound("data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data is not null
        defaultRifornimentoShouldBeFound("data.specified=true");

        // Get all the rifornimentoList where data is null
        defaultRifornimentoShouldNotBeFound("data.specified=false");
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data is greater than or equal to DEFAULT_DATA
        defaultRifornimentoShouldBeFound("data.greaterThanOrEqual=" + DEFAULT_DATA);

        // Get all the rifornimentoList where data is greater than or equal to UPDATED_DATA
        defaultRifornimentoShouldNotBeFound("data.greaterThanOrEqual=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data is less than or equal to DEFAULT_DATA
        defaultRifornimentoShouldBeFound("data.lessThanOrEqual=" + DEFAULT_DATA);

        // Get all the rifornimentoList where data is less than or equal to SMALLER_DATA
        defaultRifornimentoShouldNotBeFound("data.lessThanOrEqual=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsLessThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data is less than DEFAULT_DATA
        defaultRifornimentoShouldNotBeFound("data.lessThan=" + DEFAULT_DATA);

        // Get all the rifornimentoList where data is less than UPDATED_DATA
        defaultRifornimentoShouldBeFound("data.lessThan=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllRifornimentosByDataIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where data is greater than DEFAULT_DATA
        defaultRifornimentoShouldNotBeFound("data.greaterThan=" + DEFAULT_DATA);

        // Get all the rifornimentoList where data is greater than SMALLER_DATA
        defaultRifornimentoShouldBeFound("data.greaterThan=" + SMALLER_DATA);
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo equals to DEFAULT_PROGRESSIVO
        defaultRifornimentoShouldBeFound("progressivo.equals=" + DEFAULT_PROGRESSIVO);

        // Get all the rifornimentoList where progressivo equals to UPDATED_PROGRESSIVO
        defaultRifornimentoShouldNotBeFound("progressivo.equals=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo not equals to DEFAULT_PROGRESSIVO
        defaultRifornimentoShouldNotBeFound("progressivo.notEquals=" + DEFAULT_PROGRESSIVO);

        // Get all the rifornimentoList where progressivo not equals to UPDATED_PROGRESSIVO
        defaultRifornimentoShouldBeFound("progressivo.notEquals=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsInShouldWork() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo in DEFAULT_PROGRESSIVO or UPDATED_PROGRESSIVO
        defaultRifornimentoShouldBeFound("progressivo.in=" + DEFAULT_PROGRESSIVO + "," + UPDATED_PROGRESSIVO);

        // Get all the rifornimentoList where progressivo equals to UPDATED_PROGRESSIVO
        defaultRifornimentoShouldNotBeFound("progressivo.in=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo is not null
        defaultRifornimentoShouldBeFound("progressivo.specified=true");

        // Get all the rifornimentoList where progressivo is null
        defaultRifornimentoShouldNotBeFound("progressivo.specified=false");
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo is greater than or equal to DEFAULT_PROGRESSIVO
        defaultRifornimentoShouldBeFound("progressivo.greaterThanOrEqual=" + DEFAULT_PROGRESSIVO);

        // Get all the rifornimentoList where progressivo is greater than or equal to UPDATED_PROGRESSIVO
        defaultRifornimentoShouldNotBeFound("progressivo.greaterThanOrEqual=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo is less than or equal to DEFAULT_PROGRESSIVO
        defaultRifornimentoShouldBeFound("progressivo.lessThanOrEqual=" + DEFAULT_PROGRESSIVO);

        // Get all the rifornimentoList where progressivo is less than or equal to SMALLER_PROGRESSIVO
        defaultRifornimentoShouldNotBeFound("progressivo.lessThanOrEqual=" + SMALLER_PROGRESSIVO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsLessThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo is less than DEFAULT_PROGRESSIVO
        defaultRifornimentoShouldNotBeFound("progressivo.lessThan=" + DEFAULT_PROGRESSIVO);

        // Get all the rifornimentoList where progressivo is less than UPDATED_PROGRESSIVO
        defaultRifornimentoShouldBeFound("progressivo.lessThan=" + UPDATED_PROGRESSIVO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByProgressivoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where progressivo is greater than DEFAULT_PROGRESSIVO
        defaultRifornimentoShouldNotBeFound("progressivo.greaterThan=" + DEFAULT_PROGRESSIVO);

        // Get all the rifornimentoList where progressivo is greater than SMALLER_PROGRESSIVO
        defaultRifornimentoShouldBeFound("progressivo.greaterThan=" + SMALLER_PROGRESSIVO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati equals to DEFAULT_LITRI_EROGATI
        defaultRifornimentoShouldBeFound("litriErogati.equals=" + DEFAULT_LITRI_EROGATI);

        // Get all the rifornimentoList where litriErogati equals to UPDATED_LITRI_EROGATI
        defaultRifornimentoShouldNotBeFound("litriErogati.equals=" + UPDATED_LITRI_EROGATI);
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati not equals to DEFAULT_LITRI_EROGATI
        defaultRifornimentoShouldNotBeFound("litriErogati.notEquals=" + DEFAULT_LITRI_EROGATI);

        // Get all the rifornimentoList where litriErogati not equals to UPDATED_LITRI_EROGATI
        defaultRifornimentoShouldBeFound("litriErogati.notEquals=" + UPDATED_LITRI_EROGATI);
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsInShouldWork() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati in DEFAULT_LITRI_EROGATI or UPDATED_LITRI_EROGATI
        defaultRifornimentoShouldBeFound("litriErogati.in=" + DEFAULT_LITRI_EROGATI + "," + UPDATED_LITRI_EROGATI);

        // Get all the rifornimentoList where litriErogati equals to UPDATED_LITRI_EROGATI
        defaultRifornimentoShouldNotBeFound("litriErogati.in=" + UPDATED_LITRI_EROGATI);
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsNullOrNotNull() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati is not null
        defaultRifornimentoShouldBeFound("litriErogati.specified=true");

        // Get all the rifornimentoList where litriErogati is null
        defaultRifornimentoShouldNotBeFound("litriErogati.specified=false");
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati is greater than or equal to DEFAULT_LITRI_EROGATI
        defaultRifornimentoShouldBeFound("litriErogati.greaterThanOrEqual=" + DEFAULT_LITRI_EROGATI);

        // Get all the rifornimentoList where litriErogati is greater than or equal to UPDATED_LITRI_EROGATI
        defaultRifornimentoShouldNotBeFound("litriErogati.greaterThanOrEqual=" + UPDATED_LITRI_EROGATI);
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati is less than or equal to DEFAULT_LITRI_EROGATI
        defaultRifornimentoShouldBeFound("litriErogati.lessThanOrEqual=" + DEFAULT_LITRI_EROGATI);

        // Get all the rifornimentoList where litriErogati is less than or equal to SMALLER_LITRI_EROGATI
        defaultRifornimentoShouldNotBeFound("litriErogati.lessThanOrEqual=" + SMALLER_LITRI_EROGATI);
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsLessThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati is less than DEFAULT_LITRI_EROGATI
        defaultRifornimentoShouldNotBeFound("litriErogati.lessThan=" + DEFAULT_LITRI_EROGATI);

        // Get all the rifornimentoList where litriErogati is less than UPDATED_LITRI_EROGATI
        defaultRifornimentoShouldBeFound("litriErogati.lessThan=" + UPDATED_LITRI_EROGATI);
    }

    @Test
    @Transactional
    void getAllRifornimentosByLitriErogatiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where litriErogati is greater than DEFAULT_LITRI_EROGATI
        defaultRifornimentoShouldNotBeFound("litriErogati.greaterThan=" + DEFAULT_LITRI_EROGATI);

        // Get all the rifornimentoList where litriErogati is greater than SMALLER_LITRI_EROGATI
        defaultRifornimentoShouldBeFound("litriErogati.greaterThan=" + SMALLER_LITRI_EROGATI);
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto equals to DEFAULT_SCONTO
        defaultRifornimentoShouldBeFound("sconto.equals=" + DEFAULT_SCONTO);

        // Get all the rifornimentoList where sconto equals to UPDATED_SCONTO
        defaultRifornimentoShouldNotBeFound("sconto.equals=" + UPDATED_SCONTO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto not equals to DEFAULT_SCONTO
        defaultRifornimentoShouldNotBeFound("sconto.notEquals=" + DEFAULT_SCONTO);

        // Get all the rifornimentoList where sconto not equals to UPDATED_SCONTO
        defaultRifornimentoShouldBeFound("sconto.notEquals=" + UPDATED_SCONTO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsInShouldWork() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto in DEFAULT_SCONTO or UPDATED_SCONTO
        defaultRifornimentoShouldBeFound("sconto.in=" + DEFAULT_SCONTO + "," + UPDATED_SCONTO);

        // Get all the rifornimentoList where sconto equals to UPDATED_SCONTO
        defaultRifornimentoShouldNotBeFound("sconto.in=" + UPDATED_SCONTO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto is not null
        defaultRifornimentoShouldBeFound("sconto.specified=true");

        // Get all the rifornimentoList where sconto is null
        defaultRifornimentoShouldNotBeFound("sconto.specified=false");
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto is greater than or equal to DEFAULT_SCONTO
        defaultRifornimentoShouldBeFound("sconto.greaterThanOrEqual=" + DEFAULT_SCONTO);

        // Get all the rifornimentoList where sconto is greater than or equal to UPDATED_SCONTO
        defaultRifornimentoShouldNotBeFound("sconto.greaterThanOrEqual=" + UPDATED_SCONTO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto is less than or equal to DEFAULT_SCONTO
        defaultRifornimentoShouldBeFound("sconto.lessThanOrEqual=" + DEFAULT_SCONTO);

        // Get all the rifornimentoList where sconto is less than or equal to SMALLER_SCONTO
        defaultRifornimentoShouldNotBeFound("sconto.lessThanOrEqual=" + SMALLER_SCONTO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsLessThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto is less than DEFAULT_SCONTO
        defaultRifornimentoShouldNotBeFound("sconto.lessThan=" + DEFAULT_SCONTO);

        // Get all the rifornimentoList where sconto is less than UPDATED_SCONTO
        defaultRifornimentoShouldBeFound("sconto.lessThan=" + UPDATED_SCONTO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByScontoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where sconto is greater than DEFAULT_SCONTO
        defaultRifornimentoShouldNotBeFound("sconto.greaterThan=" + DEFAULT_SCONTO);

        // Get all the rifornimentoList where sconto is greater than SMALLER_SCONTO
        defaultRifornimentoShouldBeFound("sconto.greaterThan=" + SMALLER_SCONTO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro equals to DEFAULT_PREZZO_AL_LITRO
        defaultRifornimentoShouldBeFound("prezzoAlLitro.equals=" + DEFAULT_PREZZO_AL_LITRO);

        // Get all the rifornimentoList where prezzoAlLitro equals to UPDATED_PREZZO_AL_LITRO
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.equals=" + UPDATED_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro not equals to DEFAULT_PREZZO_AL_LITRO
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.notEquals=" + DEFAULT_PREZZO_AL_LITRO);

        // Get all the rifornimentoList where prezzoAlLitro not equals to UPDATED_PREZZO_AL_LITRO
        defaultRifornimentoShouldBeFound("prezzoAlLitro.notEquals=" + UPDATED_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsInShouldWork() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro in DEFAULT_PREZZO_AL_LITRO or UPDATED_PREZZO_AL_LITRO
        defaultRifornimentoShouldBeFound("prezzoAlLitro.in=" + DEFAULT_PREZZO_AL_LITRO + "," + UPDATED_PREZZO_AL_LITRO);

        // Get all the rifornimentoList where prezzoAlLitro equals to UPDATED_PREZZO_AL_LITRO
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.in=" + UPDATED_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsNullOrNotNull() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro is not null
        defaultRifornimentoShouldBeFound("prezzoAlLitro.specified=true");

        // Get all the rifornimentoList where prezzoAlLitro is null
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.specified=false");
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro is greater than or equal to DEFAULT_PREZZO_AL_LITRO
        defaultRifornimentoShouldBeFound("prezzoAlLitro.greaterThanOrEqual=" + DEFAULT_PREZZO_AL_LITRO);

        // Get all the rifornimentoList where prezzoAlLitro is greater than or equal to UPDATED_PREZZO_AL_LITRO
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.greaterThanOrEqual=" + UPDATED_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro is less than or equal to DEFAULT_PREZZO_AL_LITRO
        defaultRifornimentoShouldBeFound("prezzoAlLitro.lessThanOrEqual=" + DEFAULT_PREZZO_AL_LITRO);

        // Get all the rifornimentoList where prezzoAlLitro is less than or equal to SMALLER_PREZZO_AL_LITRO
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.lessThanOrEqual=" + SMALLER_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsLessThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro is less than DEFAULT_PREZZO_AL_LITRO
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.lessThan=" + DEFAULT_PREZZO_AL_LITRO);

        // Get all the rifornimentoList where prezzoAlLitro is less than UPDATED_PREZZO_AL_LITRO
        defaultRifornimentoShouldBeFound("prezzoAlLitro.lessThan=" + UPDATED_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByPrezzoAlLitroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where prezzoAlLitro is greater than DEFAULT_PREZZO_AL_LITRO
        defaultRifornimentoShouldNotBeFound("prezzoAlLitro.greaterThan=" + DEFAULT_PREZZO_AL_LITRO);

        // Get all the rifornimentoList where prezzoAlLitro is greater than SMALLER_PREZZO_AL_LITRO
        defaultRifornimentoShouldBeFound("prezzoAlLitro.greaterThan=" + SMALLER_PREZZO_AL_LITRO);
    }

    @Test
    @Transactional
    void getAllRifornimentosByTipoCarburanteIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where tipoCarburante equals to DEFAULT_TIPO_CARBURANTE
        defaultRifornimentoShouldBeFound("tipoCarburante.equals=" + DEFAULT_TIPO_CARBURANTE);

        // Get all the rifornimentoList where tipoCarburante equals to UPDATED_TIPO_CARBURANTE
        defaultRifornimentoShouldNotBeFound("tipoCarburante.equals=" + UPDATED_TIPO_CARBURANTE);
    }

    @Test
    @Transactional
    void getAllRifornimentosByTipoCarburanteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where tipoCarburante not equals to DEFAULT_TIPO_CARBURANTE
        defaultRifornimentoShouldNotBeFound("tipoCarburante.notEquals=" + DEFAULT_TIPO_CARBURANTE);

        // Get all the rifornimentoList where tipoCarburante not equals to UPDATED_TIPO_CARBURANTE
        defaultRifornimentoShouldBeFound("tipoCarburante.notEquals=" + UPDATED_TIPO_CARBURANTE);
    }

    @Test
    @Transactional
    void getAllRifornimentosByTipoCarburanteIsInShouldWork() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where tipoCarburante in DEFAULT_TIPO_CARBURANTE or UPDATED_TIPO_CARBURANTE
        defaultRifornimentoShouldBeFound("tipoCarburante.in=" + DEFAULT_TIPO_CARBURANTE + "," + UPDATED_TIPO_CARBURANTE);

        // Get all the rifornimentoList where tipoCarburante equals to UPDATED_TIPO_CARBURANTE
        defaultRifornimentoShouldNotBeFound("tipoCarburante.in=" + UPDATED_TIPO_CARBURANTE);
    }

    @Test
    @Transactional
    void getAllRifornimentosByTipoCarburanteIsNullOrNotNull() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        // Get all the rifornimentoList where tipoCarburante is not null
        defaultRifornimentoShouldBeFound("tipoCarburante.specified=true");

        // Get all the rifornimentoList where tipoCarburante is null
        defaultRifornimentoShouldNotBeFound("tipoCarburante.specified=false");
    }

    @Test
    @Transactional
    void getAllRifornimentosByGestoreIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);
        Gestore gestore = GestoreResourceIT.createEntity(em);
        em.persist(gestore);
        em.flush();
        rifornimento.setGestore(gestore);
        rifornimentoRepository.saveAndFlush(rifornimento);
        Long gestoreId = gestore.getId();

        // Get all the rifornimentoList where gestore equals to gestoreId
        defaultRifornimentoShouldBeFound("gestoreId.equals=" + gestoreId);

        // Get all the rifornimentoList where gestore equals to gestoreId + 1
        defaultRifornimentoShouldNotBeFound("gestoreId.equals=" + (gestoreId + 1));
    }

    @Test
    @Transactional
    void getAllRifornimentosByTesseraIsEqualToSomething() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);
        Tessera tessera = TesseraResourceIT.createEntity(em);
        em.persist(tessera);
        em.flush();
        rifornimento.setTessera(tessera);
        rifornimentoRepository.saveAndFlush(rifornimento);
        Long tesseraId = tessera.getId();

        // Get all the rifornimentoList where tessera equals to tesseraId
        defaultRifornimentoShouldBeFound("tesseraId.equals=" + tesseraId);

        // Get all the rifornimentoList where tessera equals to tesseraId + 1
        defaultRifornimentoShouldNotBeFound("tesseraId.equals=" + (tesseraId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRifornimentoShouldBeFound(String filter) throws Exception {
        restRifornimentoMockMvc
            .perform(get("/api/rifornimentos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rifornimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].progressivo").value(hasItem(DEFAULT_PROGRESSIVO)))
            .andExpect(jsonPath("$.[*].litriErogati").value(hasItem(DEFAULT_LITRI_EROGATI.doubleValue())))
            .andExpect(jsonPath("$.[*].sconto").value(hasItem(DEFAULT_SCONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].prezzoAlLitro").value(hasItem(DEFAULT_PREZZO_AL_LITRO.doubleValue())))
            .andExpect(jsonPath("$.[*].tipoCarburante").value(hasItem(DEFAULT_TIPO_CARBURANTE.toString())));

        // Check, that the count call also returns 1
        restRifornimentoMockMvc
            .perform(get("/api/rifornimentos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRifornimentoShouldNotBeFound(String filter) throws Exception {
        restRifornimentoMockMvc
            .perform(get("/api/rifornimentos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRifornimentoMockMvc
            .perform(get("/api/rifornimentos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRifornimento() throws Exception {
        // Get the rifornimento
        restRifornimentoMockMvc.perform(get("/api/rifornimentos/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateRifornimento() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        int databaseSizeBeforeUpdate = rifornimentoRepository.findAll().size();

        // Update the rifornimento
        Rifornimento updatedRifornimento = rifornimentoRepository.findById(rifornimento.getId()).get();
        // Disconnect from session so that the updates on updatedRifornimento are not directly saved in db
        em.detach(updatedRifornimento);
        updatedRifornimento
            .data(UPDATED_DATA)
            .progressivo(UPDATED_PROGRESSIVO)
            .litriErogati(UPDATED_LITRI_EROGATI)
            .sconto(UPDATED_SCONTO)
            .prezzoAlLitro(UPDATED_PREZZO_AL_LITRO)
            .tipoCarburante(UPDATED_TIPO_CARBURANTE);

        restRifornimentoMockMvc
            .perform(
                put("/api/rifornimentos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRifornimento))
            )
            .andExpect(status().isOk());

        // Validate the Rifornimento in the database
        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeUpdate);
        Rifornimento testRifornimento = rifornimentoList.get(rifornimentoList.size() - 1);
        assertThat(testRifornimento.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testRifornimento.getProgressivo()).isEqualTo(UPDATED_PROGRESSIVO);
        assertThat(testRifornimento.getLitriErogati()).isEqualTo(UPDATED_LITRI_EROGATI);
        assertThat(testRifornimento.getSconto()).isEqualTo(UPDATED_SCONTO);
        assertThat(testRifornimento.getPrezzoAlLitro()).isEqualTo(UPDATED_PREZZO_AL_LITRO);
        assertThat(testRifornimento.getTipoCarburante()).isEqualTo(UPDATED_TIPO_CARBURANTE);
    }

    @Test
    @Transactional
    void updateNonExistingRifornimento() throws Exception {
        int databaseSizeBeforeUpdate = rifornimentoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRifornimentoMockMvc
            .perform(
                put("/api/rifornimentos").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rifornimento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rifornimento in the database
        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRifornimentoWithPatch() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        int databaseSizeBeforeUpdate = rifornimentoRepository.findAll().size();

        // Update the rifornimento using partial update
        Rifornimento partialUpdatedRifornimento = new Rifornimento();
        partialUpdatedRifornimento.setId(rifornimento.getId());

        partialUpdatedRifornimento.data(UPDATED_DATA).progressivo(UPDATED_PROGRESSIVO).prezzoAlLitro(UPDATED_PREZZO_AL_LITRO);

        restRifornimentoMockMvc
            .perform(
                patch("/api/rifornimentos")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRifornimento))
            )
            .andExpect(status().isOk());

        // Validate the Rifornimento in the database
        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeUpdate);
        Rifornimento testRifornimento = rifornimentoList.get(rifornimentoList.size() - 1);
        assertThat(testRifornimento.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testRifornimento.getProgressivo()).isEqualTo(UPDATED_PROGRESSIVO);
        assertThat(testRifornimento.getLitriErogati()).isEqualTo(DEFAULT_LITRI_EROGATI);
        assertThat(testRifornimento.getSconto()).isEqualTo(DEFAULT_SCONTO);
        assertThat(testRifornimento.getPrezzoAlLitro()).isEqualTo(UPDATED_PREZZO_AL_LITRO);
        assertThat(testRifornimento.getTipoCarburante()).isEqualTo(DEFAULT_TIPO_CARBURANTE);
    }

    @Test
    @Transactional
    void fullUpdateRifornimentoWithPatch() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        int databaseSizeBeforeUpdate = rifornimentoRepository.findAll().size();

        // Update the rifornimento using partial update
        Rifornimento partialUpdatedRifornimento = new Rifornimento();
        partialUpdatedRifornimento.setId(rifornimento.getId());

        partialUpdatedRifornimento
            .data(UPDATED_DATA)
            .progressivo(UPDATED_PROGRESSIVO)
            .litriErogati(UPDATED_LITRI_EROGATI)
            .sconto(UPDATED_SCONTO)
            .prezzoAlLitro(UPDATED_PREZZO_AL_LITRO)
            .tipoCarburante(UPDATED_TIPO_CARBURANTE);

        restRifornimentoMockMvc
            .perform(
                patch("/api/rifornimentos")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRifornimento))
            )
            .andExpect(status().isOk());

        // Validate the Rifornimento in the database
        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeUpdate);
        Rifornimento testRifornimento = rifornimentoList.get(rifornimentoList.size() - 1);
        assertThat(testRifornimento.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testRifornimento.getProgressivo()).isEqualTo(UPDATED_PROGRESSIVO);
        assertThat(testRifornimento.getLitriErogati()).isEqualTo(UPDATED_LITRI_EROGATI);
        assertThat(testRifornimento.getSconto()).isEqualTo(UPDATED_SCONTO);
        assertThat(testRifornimento.getPrezzoAlLitro()).isEqualTo(UPDATED_PREZZO_AL_LITRO);
        assertThat(testRifornimento.getTipoCarburante()).isEqualTo(UPDATED_TIPO_CARBURANTE);
    }

    @Test
    @Transactional
    void partialUpdateRifornimentoShouldThrown() throws Exception {
        // Update the rifornimento without id should throw
        Rifornimento partialUpdatedRifornimento = new Rifornimento();

        restRifornimentoMockMvc
            .perform(
                patch("/api/rifornimentos")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRifornimento))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteRifornimento() throws Exception {
        // Initialize the database
        rifornimentoRepository.saveAndFlush(rifornimento);

        int databaseSizeBeforeDelete = rifornimentoRepository.findAll().size();

        // Delete the rifornimento
        restRifornimentoMockMvc
            .perform(delete("/api/rifornimentos/{id}", rifornimento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rifornimento> rifornimentoList = rifornimentoRepository.findAll();
        assertThat(rifornimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
