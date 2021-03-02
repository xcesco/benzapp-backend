package it.insiel.innovazione.poc.benzapp.web.rest;

import it.insiel.innovazione.poc.benzapp.domain.Fascia;
import it.insiel.innovazione.poc.benzapp.service.FasciaService;
import it.insiel.innovazione.poc.benzapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.insiel.innovazione.poc.benzapp.domain.Fascia}.
 */
@RestController
@RequestMapping("/api")
public class FasciaResource {

    private final Logger log = LoggerFactory.getLogger(FasciaResource.class);

    private static final String ENTITY_NAME = "fascia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FasciaService fasciaService;

    public FasciaResource(FasciaService fasciaService) {
        this.fasciaService = fasciaService;
    }

    /**
     * {@code POST  /fascias} : Create a new fascia.
     *
     * @param fascia the fascia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fascia, or with status {@code 400 (Bad Request)} if the fascia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fascias")
    public ResponseEntity<Fascia> createFascia(@RequestBody Fascia fascia) throws URISyntaxException {
        log.debug("REST request to save Fascia : {}", fascia);
        if (fascia.getId() != null) {
            throw new BadRequestAlertException("A new fascia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fascia result = fasciaService.save(fascia);
        return ResponseEntity
            .created(new URI("/api/fascias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fascias} : Updates an existing fascia.
     *
     * @param fascia the fascia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fascia,
     * or with status {@code 400 (Bad Request)} if the fascia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fascia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fascias")
    public ResponseEntity<Fascia> updateFascia(@RequestBody Fascia fascia) throws URISyntaxException {
        log.debug("REST request to update Fascia : {}", fascia);
        if (fascia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fascia result = fasciaService.save(fascia);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fascia.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fascias} : Updates given fields of an existing fascia.
     *
     * @param fascia the fascia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fascia,
     * or with status {@code 400 (Bad Request)} if the fascia is not valid,
     * or with status {@code 404 (Not Found)} if the fascia is not found,
     * or with status {@code 500 (Internal Server Error)} if the fascia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fascias", consumes = "application/merge-patch+json")
    public ResponseEntity<Fascia> partialUpdateFascia(@RequestBody Fascia fascia) throws URISyntaxException {
        log.debug("REST request to update Fascia partially : {}", fascia);
        if (fascia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Fascia> result = fasciaService.partialUpdate(fascia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fascia.getId().toString())
        );
    }

    /**
     * {@code GET  /fascias} : get all the fascias.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fascias in body.
     */
    @GetMapping("/fascias")
    public List<Fascia> getAllFascias() {
        log.debug("REST request to get all Fascias");
        return fasciaService.findAll();
    }

    /**
     * {@code GET  /fascias/:id} : get the "id" fascia.
     *
     * @param id the id of the fascia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fascia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fascias/{id}")
    public ResponseEntity<Fascia> getFascia(@PathVariable Long id) {
        log.debug("REST request to get Fascia : {}", id);
        Optional<Fascia> fascia = fasciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fascia);
    }

    /**
     * {@code DELETE  /fascias/:id} : delete the "id" fascia.
     *
     * @param id the id of the fascia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fascias/{id}")
    public ResponseEntity<Void> deleteFascia(@PathVariable Long id) {
        log.debug("REST request to delete Fascia : {}", id);
        fasciaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
