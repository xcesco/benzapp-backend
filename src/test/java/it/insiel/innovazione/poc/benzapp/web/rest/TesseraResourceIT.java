package it.insiel.innovazione.poc.benzapp.web.rest;

import static it.insiel.innovazione.poc.benzapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoVeicolo;
import it.insiel.innovazione.poc.benzapp.repository.TesseraRepository;
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
