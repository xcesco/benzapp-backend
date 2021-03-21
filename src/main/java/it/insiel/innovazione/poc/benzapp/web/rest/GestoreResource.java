package it.insiel.innovazione.poc.benzapp.web.rest;

import it.insiel.innovazione.poc.benzapp.domain.Gestore;
import it.insiel.innovazione.poc.benzapp.service.GestoreQueryService;
import it.insiel.innovazione.poc.benzapp.service.GestoreService;
import it.insiel.innovazione.poc.benzapp.service.dto.GestoreCriteria;
import it.insiel.innovazione.poc.benzapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.insiel.innovazione.poc.benzapp.domain.Gestore}.
 */
@RestController
@RequestMapping("/api")
public class GestoreResource {

    private final Logger log = LoggerFactory.getLogger(GestoreResource.class);

    private static final String ENTITY_NAME = "gestore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GestoreService gestoreService;

    private final GestoreQueryService gestoreQueryService;

    public GestoreResource(GestoreService gestoreService, GestoreQueryService gestoreQueryService) {
        this.gestoreService = gestoreService;
        this.gestoreQueryService = gestoreQueryService;
    }

    /**
     * {@code POST  /gestores} : Create a new gestore.
     *
     * @param gestore the gestore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gestore, or with status {@code 400 (Bad Request)} if the gestore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gestores")
    public ResponseEntity<Gestore> createGestore(@RequestBody Gestore gestore) throws URISyntaxException {
        log.debug("REST request to save Gestore : {}", gestore);
        if (gestore.getId() != null) {
            throw new BadRequestAlertException("A new gestore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gestore result = gestoreService.save(gestore);
        return ResponseEntity
            .created(new URI("/api/gestores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gestores} : Updates an existing gestore.
     *
     * @param gestore the gestore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gestore,
     * or with status {@code 400 (Bad Request)} if the gestore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gestore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gestores")
    public ResponseEntity<Gestore> updateGestore(@RequestBody Gestore gestore) throws URISyntaxException {
        log.debug("REST request to update Gestore : {}", gestore);
        if (gestore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Gestore result = gestoreService.save(gestore);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gestore.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gestores} : Updates given fields of an existing gestore.
     *
     * @param gestore the gestore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gestore,
     * or with status {@code 400 (Bad Request)} if the gestore is not valid,
     * or with status {@code 404 (Not Found)} if the gestore is not found,
     * or with status {@code 500 (Internal Server Error)} if the gestore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gestores", consumes = "application/merge-patch+json")
    public ResponseEntity<Gestore> partialUpdateGestore(@RequestBody Gestore gestore) throws URISyntaxException {
        log.debug("REST request to update Gestore partially : {}", gestore);
        if (gestore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Gestore> result = gestoreService.partialUpdate(gestore);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gestore.getId().toString())
        );
    }

    /**
     * {@code GET  /gestores} : get all the gestores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gestores in body.
     */
    @GetMapping("/gestores")
    public ResponseEntity<List<Gestore>> getAllGestores(GestoreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Gestores by criteria: {}", criteria);
        Page<Gestore> page = gestoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gestores/count} : count all the gestores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gestores/count")
    public ResponseEntity<Long> countGestores(GestoreCriteria criteria) {
        log.debug("REST request to count Gestores by criteria: {}", criteria);
        return ResponseEntity.ok().body(gestoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gestores/:id} : get the "id" gestore.
     *
     * @param id the id of the gestore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gestore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gestores/{id}")
    public ResponseEntity<Gestore> getGestore(@PathVariable Long id) {
        log.debug("REST request to get Gestore : {}", id);
        Optional<Gestore> gestore = gestoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gestore);
    }

    /**
     * {@code DELETE  /gestores/:id} : delete the "id" gestore.
     *
     * @param id the id of the gestore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gestores/{id}")
    public ResponseEntity<Void> deleteGestore(@PathVariable Long id) {
        log.debug("REST request to delete Gestore : {}", id);
        gestoreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
