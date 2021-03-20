package it.insiel.innovazione.poc.benzapp.web.rest;

import it.insiel.innovazione.poc.benzapp.domain.Delega;
import it.insiel.innovazione.poc.benzapp.service.DelegaService;
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
 * REST controller for managing {@link it.insiel.innovazione.poc.benzapp.domain.Delega}.
 */
@RestController
@RequestMapping("/api")
public class DelegaResource {

    private final Logger log = LoggerFactory.getLogger(DelegaResource.class);

    private static final String ENTITY_NAME = "delega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DelegaService delegaService;

    public DelegaResource(DelegaService delegaService) {
        this.delegaService = delegaService;
    }

    /**
     * {@code POST  /delegas} : Create a new delega.
     *
     * @param delega the delega to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new delega, or with status {@code 400 (Bad Request)} if the delega has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delegas")
    public ResponseEntity<Delega> createDelega(@RequestBody Delega delega) throws URISyntaxException {
        log.debug("REST request to save Delega : {}", delega);
        if (delega.getId() != null) {
            throw new BadRequestAlertException("A new delega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Delega result = delegaService.save(delega);
        return ResponseEntity
            .created(new URI("/api/delegas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delegas} : Updates an existing delega.
     *
     * @param delega the delega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delega,
     * or with status {@code 400 (Bad Request)} if the delega is not valid,
     * or with status {@code 500 (Internal Server Error)} if the delega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delegas")
    public ResponseEntity<Delega> updateDelega(@RequestBody Delega delega) throws URISyntaxException {
        log.debug("REST request to update Delega : {}", delega);
        if (delega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Delega result = delegaService.save(delega);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delega.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delegas} : Updates given fields of an existing delega.
     *
     * @param delega the delega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delega,
     * or with status {@code 400 (Bad Request)} if the delega is not valid,
     * or with status {@code 404 (Not Found)} if the delega is not found,
     * or with status {@code 500 (Internal Server Error)} if the delega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delegas", consumes = "application/merge-patch+json")
    public ResponseEntity<Delega> partialUpdateDelega(@RequestBody Delega delega) throws URISyntaxException {
        log.debug("REST request to update Delega partially : {}", delega);
        if (delega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Delega> result = delegaService.partialUpdate(delega);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delega.getId().toString())
        );
    }

    /**
     * {@code GET  /delegas} : get all the delegas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of delegas in body.
     */
    @GetMapping("/delegas")
    public ResponseEntity<List<Delega>> getAllDelegas(Pageable pageable) {
        log.debug("REST request to get a page of Delegas");
        Page<Delega> page = delegaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delegas/:id} : get the "id" delega.
     *
     * @param id the id of the delega to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the delega, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delegas/{id}")
    public ResponseEntity<Delega> getDelega(@PathVariable Long id) {
        log.debug("REST request to get Delega : {}", id);
        Optional<Delega> delega = delegaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(delega);
    }

    /**
     * {@code DELETE  /delegas/:id} : delete the "id" delega.
     *
     * @param id the id of the delega to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delegas/{id}")
    public ResponseEntity<Void> deleteDelega(@PathVariable Long id) {
        log.debug("REST request to delete Delega : {}", id);
        delegaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
