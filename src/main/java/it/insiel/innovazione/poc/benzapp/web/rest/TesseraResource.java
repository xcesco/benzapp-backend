package it.insiel.innovazione.poc.benzapp.web.rest;

import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import it.insiel.innovazione.poc.benzapp.service.TesseraService;
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
 * REST controller for managing {@link it.insiel.innovazione.poc.benzapp.domain.Tessera}.
 */
@RestController
@RequestMapping("/api")
public class TesseraResource {

    private final Logger log = LoggerFactory.getLogger(TesseraResource.class);

    private static final String ENTITY_NAME = "tessera";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TesseraService tesseraService;

    public TesseraResource(TesseraService tesseraService) {
        this.tesseraService = tesseraService;
    }

    /**
     * {@code POST  /tesseras} : Create a new tessera.
     *
     * @param tessera the tessera to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tessera, or with status {@code 400 (Bad Request)} if the tessera has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tesseras")
    public ResponseEntity<Tessera> createTessera(@Valid @RequestBody Tessera tessera) throws URISyntaxException {
        log.debug("REST request to save Tessera : {}", tessera);
        if (tessera.getId() != null) {
            throw new BadRequestAlertException("A new tessera cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tessera result = tesseraService.save(tessera);
        return ResponseEntity
            .created(new URI("/api/tesseras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tesseras} : Updates an existing tessera.
     *
     * @param tessera the tessera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tessera,
     * or with status {@code 400 (Bad Request)} if the tessera is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tessera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tesseras")
    public ResponseEntity<Tessera> updateTessera(@Valid @RequestBody Tessera tessera) throws URISyntaxException {
        log.debug("REST request to update Tessera : {}", tessera);
        if (tessera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tessera result = tesseraService.save(tessera);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tessera.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tesseras} : Updates given fields of an existing tessera.
     *
     * @param tessera the tessera to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tessera,
     * or with status {@code 400 (Bad Request)} if the tessera is not valid,
     * or with status {@code 404 (Not Found)} if the tessera is not found,
     * or with status {@code 500 (Internal Server Error)} if the tessera couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tesseras", consumes = "application/merge-patch+json")
    public ResponseEntity<Tessera> partialUpdateTessera(@NotNull @RequestBody Tessera tessera) throws URISyntaxException {
        log.debug("REST request to update Tessera partially : {}", tessera);
        if (tessera.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Tessera> result = tesseraService.partialUpdate(tessera);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tessera.getId().toString())
        );
    }

    /**
     * {@code GET  /tesseras} : get all the tesseras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tesseras in body.
     */
    @GetMapping("/tesseras")
    public ResponseEntity<List<Tessera>> getAllTesseras(Pageable pageable) {
        log.debug("REST request to get a page of Tesseras");
        Page<Tessera> page = tesseraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tesseras/:id} : get the "id" tessera.
     *
     * @param id the id of the tessera to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tessera, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tesseras/{id}")
    public ResponseEntity<Tessera> getTessera(@PathVariable Long id) {
        log.debug("REST request to get Tessera : {}", id);
        Optional<Tessera> tessera = tesseraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tessera);
    }

    /**
     * {@code DELETE  /tesseras/:id} : delete the "id" tessera.
     *
     * @param id the id of the tessera to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tesseras/{id}")
    public ResponseEntity<Void> deleteTessera(@PathVariable Long id) {
        log.debug("REST request to delete Tessera : {}", id);
        tesseraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
