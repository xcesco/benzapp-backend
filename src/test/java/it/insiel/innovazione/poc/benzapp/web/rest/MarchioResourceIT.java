package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Marchio;
import it.insiel.innovazione.poc.benzapp.repository.MarchioRepository;
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
 * Integration tests for the {@link MarchioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MarchioResourceIT {

    private static final byte[] DEFAULT_IMMAGINE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMMAGINE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMMAGINE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMMAGINE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private MarchioRepository marchioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMarchioMockMvc;

    private Marchio marchio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marchio createEntity(EntityManager em) {
        Marchio marchio = new Marchio().immagine(DEFAULT_IMMAGINE).immagineContentType(DEFAULT_IMMAGINE_CONTENT_TYPE).nome(DEFAULT_NOME);
        return marchio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Marchio createUpdatedEntity(EntityManager em) {
        Marchio marchio = new Marchio().immagine(UPDATED_IMMAGINE).immagineContentType(UPDATED_IMMAGINE_CONTENT_TYPE).nome(UPDATED_NOME);
        return marchio;
    }

    @BeforeEach
    public void initTest() {
        marchio = createEntity(em);
    }

    @Test
    @Transactional
    void createMarchio() throws Exception {
        int databaseSizeBeforeCreate = marchioRepository.findAll().size();
        // Create the Marchio
        restMarchioMockMvc
            .perform(post("/api/marchios").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marchio)))
            .andExpect(status().isCreated());

        // Validate the Marchio in the database
        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeCreate + 1);
        Marchio testMarchio = marchioList.get(marchioList.size() - 1);
        assertThat(testMarchio.getImmagine()).isEqualTo(DEFAULT_IMMAGINE);
        assertThat(testMarchio.getImmagineContentType()).isEqualTo(DEFAULT_IMMAGINE_CONTENT_TYPE);
        assertThat(testMarchio.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createMarchioWithExistingId() throws Exception {
        // Create the Marchio with an existing ID
        marchio.setId(1L);

        int databaseSizeBeforeCreate = marchioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarchioMockMvc
            .perform(post("/api/marchios").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marchio)))
            .andExpect(status().isBadRequest());

        // Validate the Marchio in the database
        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = marchioRepository.findAll().size();
        // set the field null
        marchio.setNome(null);

        // Create the Marchio, which fails.

        restMarchioMockMvc
            .perform(post("/api/marchios").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marchio)))
            .andExpect(status().isBadRequest());

        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMarchios() throws Exception {
        // Initialize the database
        marchioRepository.saveAndFlush(marchio);

        // Get all the marchioList
        restMarchioMockMvc
            .perform(get("/api/marchios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marchio.getId().intValue())))
            .andExpect(jsonPath("$.[*].immagineContentType").value(hasItem(DEFAULT_IMMAGINE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].immagine").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMMAGINE))))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getMarchio() throws Exception {
        // Initialize the database
        marchioRepository.saveAndFlush(marchio);

        // Get the marchio
        restMarchioMockMvc
            .perform(get("/api/marchios/{id}", marchio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(marchio.getId().intValue()))
            .andExpect(jsonPath("$.immagineContentType").value(DEFAULT_IMMAGINE_CONTENT_TYPE))
            .andExpect(jsonPath("$.immagine").value(Base64Utils.encodeToString(DEFAULT_IMMAGINE)))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getNonExistingMarchio() throws Exception {
        // Get the marchio
        restMarchioMockMvc.perform(get("/api/marchios/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateMarchio() throws Exception {
        // Initialize the database
        marchioRepository.saveAndFlush(marchio);

        int databaseSizeBeforeUpdate = marchioRepository.findAll().size();

        // Update the marchio
        Marchio updatedMarchio = marchioRepository.findById(marchio.getId()).get();
        // Disconnect from session so that the updates on updatedMarchio are not directly saved in db
        em.detach(updatedMarchio);
        updatedMarchio.immagine(UPDATED_IMMAGINE).immagineContentType(UPDATED_IMMAGINE_CONTENT_TYPE).nome(UPDATED_NOME);

        restMarchioMockMvc
            .perform(
                put("/api/marchios").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedMarchio))
            )
            .andExpect(status().isOk());

        // Validate the Marchio in the database
        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeUpdate);
        Marchio testMarchio = marchioList.get(marchioList.size() - 1);
        assertThat(testMarchio.getImmagine()).isEqualTo(UPDATED_IMMAGINE);
        assertThat(testMarchio.getImmagineContentType()).isEqualTo(UPDATED_IMMAGINE_CONTENT_TYPE);
        assertThat(testMarchio.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void updateNonExistingMarchio() throws Exception {
        int databaseSizeBeforeUpdate = marchioRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarchioMockMvc
            .perform(put("/api/marchios").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(marchio)))
            .andExpect(status().isBadRequest());

        // Validate the Marchio in the database
        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMarchioWithPatch() throws Exception {
        // Initialize the database
        marchioRepository.saveAndFlush(marchio);

        int databaseSizeBeforeUpdate = marchioRepository.findAll().size();

        // Update the marchio using partial update
        Marchio partialUpdatedMarchio = new Marchio();
        partialUpdatedMarchio.setId(marchio.getId());

        partialUpdatedMarchio.nome(UPDATED_NOME);

        restMarchioMockMvc
            .perform(
                patch("/api/marchios")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarchio))
            )
            .andExpect(status().isOk());

        // Validate the Marchio in the database
        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeUpdate);
        Marchio testMarchio = marchioList.get(marchioList.size() - 1);
        assertThat(testMarchio.getImmagine()).isEqualTo(DEFAULT_IMMAGINE);
        assertThat(testMarchio.getImmagineContentType()).isEqualTo(DEFAULT_IMMAGINE_CONTENT_TYPE);
        assertThat(testMarchio.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateMarchioWithPatch() throws Exception {
        // Initialize the database
        marchioRepository.saveAndFlush(marchio);

        int databaseSizeBeforeUpdate = marchioRepository.findAll().size();

        // Update the marchio using partial update
        Marchio partialUpdatedMarchio = new Marchio();
        partialUpdatedMarchio.setId(marchio.getId());

        partialUpdatedMarchio.immagine(UPDATED_IMMAGINE).immagineContentType(UPDATED_IMMAGINE_CONTENT_TYPE).nome(UPDATED_NOME);

        restMarchioMockMvc
            .perform(
                patch("/api/marchios")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarchio))
            )
            .andExpect(status().isOk());

        // Validate the Marchio in the database
        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeUpdate);
        Marchio testMarchio = marchioList.get(marchioList.size() - 1);
        assertThat(testMarchio.getImmagine()).isEqualTo(UPDATED_IMMAGINE);
        assertThat(testMarchio.getImmagineContentType()).isEqualTo(UPDATED_IMMAGINE_CONTENT_TYPE);
        assertThat(testMarchio.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void partialUpdateMarchioShouldThrown() throws Exception {
        // Update the marchio without id should throw
        Marchio partialUpdatedMarchio = new Marchio();

        restMarchioMockMvc
            .perform(
                patch("/api/marchios")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMarchio))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteMarchio() throws Exception {
        // Initialize the database
        marchioRepository.saveAndFlush(marchio);

        int databaseSizeBeforeDelete = marchioRepository.findAll().size();

        // Delete the marchio
        restMarchioMockMvc
            .perform(delete("/api/marchios/{id}", marchio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Marchio> marchioList = marchioRepository.findAll();
        assertThat(marchioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
