package it.insiel.innovazione.poc.benzapp.web.rest;

import static it.insiel.innovazione.poc.benzapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
import it.insiel.innovazione.poc.benzapp.repository.RifornimentoRepository;
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

    private static final Integer DEFAULT_PROGRESSIVO = 1;
    private static final Integer UPDATED_PROGRESSIVO = 2;

    private static final Float DEFAULT_LITRI_EROGATI = 1F;
    private static final Float UPDATED_LITRI_EROGATI = 2F;

    private static final Float DEFAULT_SCONTO = 1F;
    private static final Float UPDATED_SCONTO = 2F;

    private static final Float DEFAULT_PREZZO_AL_LITRO = 1F;
    private static final Float UPDATED_PREZZO_AL_LITRO = 2F;

    private static final TipoCarburante DEFAULT_TIPO_CARBURANTE = TipoCarburante.BENZINA;
    private static final TipoCarburante UPDATED_TIPO_CARBURANTE = TipoCarburante.DIESEL;

    @Autowired
    private RifornimentoRepository rifornimentoRepository;

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
