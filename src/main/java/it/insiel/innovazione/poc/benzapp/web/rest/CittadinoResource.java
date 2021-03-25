package it.insiel.innovazione.poc.benzapp.web.rest;

import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import it.insiel.innovazione.poc.benzapp.security.SecurityUtils;
import it.insiel.innovazione.poc.benzapp.service.CittadinoQueryService;
import it.insiel.innovazione.poc.benzapp.service.CittadinoService;
import it.insiel.innovazione.poc.benzapp.service.dto.CittadinoCriteria;
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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.insiel.innovazione.poc.benzapp.domain.Cittadino}.
 */
@RestController
@RequestMapping("/api")
public class CittadinoResource {

    private final Logger log = LoggerFactory.getLogger(CittadinoResource.class);

    private static final String ENTITY_NAME = "cittadino";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CittadinoService cittadinoService;

    private final CittadinoQueryService cittadinoQueryService;

    public CittadinoResource(CittadinoService cittadinoService, CittadinoQueryService cittadinoQueryService) {
        this.cittadinoService = cittadinoService;
        this.cittadinoQueryService = cittadinoQueryService;
    }

    /**
     * {@code POST  /cittadinos} : Create a new cittadino.
     *
     * @param cittadino the cittadino to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cittadino, or with status {@code 400 (Bad Request)} if the cittadino has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cittadinos")
    public ResponseEntity<Cittadino> createCittadino(@RequestBody Cittadino cittadino) throws URISyntaxException {
        log.debug("REST request to save Cittadino : {}", cittadino);
        if (cittadino.getId() != null) {
            throw new BadRequestAlertException("A new cittadino cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cittadino result = cittadinoService.save(cittadino);
        return ResponseEntity
            .created(new URI("/api/cittadinos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cittadinos} : Updates an existing cittadino.
     *
     * @param cittadino the cittadino to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cittadino,
     * or with status {@code 400 (Bad Request)} if the cittadino is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cittadino couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cittadinos")
    public ResponseEntity<Cittadino> updateCittadino(@RequestBody Cittadino cittadino) throws URISyntaxException {
        log.debug("REST request to update Cittadino : {}", cittadino);
        if (cittadino.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cittadino result = cittadinoService.save(cittadino);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cittadino.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cittadinos} : Updates given fields of an existing cittadino.
     *
     * @param cittadino the cittadino to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cittadino,
     * or with status {@code 400 (Bad Request)} if the cittadino is not valid,
     * or with status {@code 404 (Not Found)} if the cittadino is not found,
     * or with status {@code 500 (Internal Server Error)} if the cittadino couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cittadinos", consumes = "application/merge-patch+json")
    public ResponseEntity<Cittadino> partialUpdateCittadino(@RequestBody Cittadino cittadino) throws URISyntaxException {
        log.debug("REST request to update Cittadino partially : {}", cittadino);
        if (cittadino.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Cittadino> result = cittadinoService.partialUpdate(cittadino);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cittadino.getId().toString())
        );
    }

    /**
     * {@code GET  /cittadinos} : get all the cittadinos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cittadinos in body.
     */
    @GetMapping("/cittadinos")
    public ResponseEntity<List<Cittadino>> getAllCittadinos(CittadinoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cittadinos by criteria: {}", criteria);

        Page<Cittadino> page = cittadinoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cittadinos/count} : count all the cittadinos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cittadinos/count")
    public ResponseEntity<Long> countCittadinos(CittadinoCriteria criteria) {
        log.debug("REST request to count Cittadinos by criteria: {}", criteria);
        return ResponseEntity.ok().body(cittadinoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cittadinos/:id} : get the "id" cittadino.
     *
     * @param id the id of the cittadino to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cittadino, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cittadinos/{id}")
    public ResponseEntity<Cittadino> getCittadino(@PathVariable Long id) {
        log.debug("REST request to get Cittadino : {}", id);
        Optional<Cittadino> cittadino = cittadinoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cittadino);
    }

    /**
     * {@code DELETE  /cittadinos/:id} : delete the "id" cittadino.
     *
     * @param id the id of the cittadino to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cittadinos/{id}")
    public ResponseEntity<Void> deleteCittadino(@PathVariable Long id) {
        log.debug("REST request to delete Cittadino : {}", id);
        cittadinoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
