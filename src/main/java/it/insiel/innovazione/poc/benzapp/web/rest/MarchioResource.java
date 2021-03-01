package it.insiel.innovazione.poc.benzapp.web.rest;

import it.insiel.innovazione.poc.benzapp.domain.Marchio;
import it.insiel.innovazione.poc.benzapp.service.MarchioService;
import it.insiel.innovazione.poc.benzapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link it.insiel.innovazione.poc.benzapp.domain.Marchio}.
 */
@RestController
@RequestMapping("/api")
public class MarchioResource {

    private final Logger log = LoggerFactory.getLogger(MarchioResource.class);

    private static final String ENTITY_NAME = "marchio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarchioService marchioService;

    public MarchioResource(MarchioService marchioService) {
        this.marchioService = marchioService;
    }

    /**
     * {@code POST  /marchios} : Create a new marchio.
     *
     * @param marchio the marchio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marchio, or with status {@code 400 (Bad Request)} if the marchio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marchios")
    public ResponseEntity<Marchio> createMarchio(@Valid @RequestBody Marchio marchio) throws URISyntaxException {
        log.debug("REST request to save Marchio : {}", marchio);
        if (marchio.getId() != null) {
            throw new BadRequestAlertException("A new marchio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Marchio result = marchioService.save(marchio);
        return ResponseEntity
            .created(new URI("/api/marchios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marchios} : Updates an existing marchio.
     *
     * @param marchio the marchio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marchio,
     * or with status {@code 400 (Bad Request)} if the marchio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marchio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marchios")
    public ResponseEntity<Marchio> updateMarchio(@Valid @RequestBody Marchio marchio) throws URISyntaxException {
        log.debug("REST request to update Marchio : {}", marchio);
        if (marchio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Marchio result = marchioService.save(marchio);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marchio.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /marchios} : Updates given fields of an existing marchio.
     *
     * @param marchio the marchio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marchio,
     * or with status {@code 400 (Bad Request)} if the marchio is not valid,
     * or with status {@code 404 (Not Found)} if the marchio is not found,
     * or with status {@code 500 (Internal Server Error)} if the marchio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marchios", consumes = "application/merge-patch+json")
    public ResponseEntity<Marchio> partialUpdateMarchio(@NotNull @RequestBody Marchio marchio) throws URISyntaxException {
        log.debug("REST request to update Marchio partially : {}", marchio);
        if (marchio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Marchio> result = marchioService.partialUpdate(marchio);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marchio.getId().toString())
        );
    }

    /**
     * {@code GET  /marchios} : get all the marchios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marchios in body.
     */
    @GetMapping("/marchios")
    public ResponseEntity<List<Marchio>> getAllMarchios(Pageable pageable) {
        log.debug("REST request to get a page of Marchios");
        Page<Marchio> page = marchioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marchios/:id} : get the "id" marchio.
     *
     * @param id the id of the marchio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marchio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marchios/{id}")
    public ResponseEntity<Marchio> getMarchio(@PathVariable Long id) {
        log.debug("REST request to get Marchio : {}", id);
        Optional<Marchio> marchio = marchioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marchio);
    }

    /**
     * {@code DELETE  /marchios/:id} : delete the "id" marchio.
     *
     * @param id the id of the marchio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marchios/{id}")
    public ResponseEntity<Void> deleteMarchio(@PathVariable Long id) {
        log.debug("REST request to delete Marchio : {}", id);
        marchioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
