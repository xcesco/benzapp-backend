package it.insiel.innovazione.poc.benzapp.web.rest;

import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.service.RifornimentoService;
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
 * REST controller for managing {@link it.insiel.innovazione.poc.benzapp.domain.Rifornimento}.
 */
@RestController
@RequestMapping("/api")
public class RifornimentoResource {

    private final Logger log = LoggerFactory.getLogger(RifornimentoResource.class);

    private static final String ENTITY_NAME = "rifornimento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RifornimentoService rifornimentoService;

    public RifornimentoResource(RifornimentoService rifornimentoService) {
        this.rifornimentoService = rifornimentoService;
    }

    /**
     * {@code POST  /rifornimentos} : Create a new rifornimento.
     *
     * @param rifornimento the rifornimento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rifornimento, or with status {@code 400 (Bad Request)} if the rifornimento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rifornimentos")
    public ResponseEntity<Rifornimento> createRifornimento(@Valid @RequestBody Rifornimento rifornimento) throws URISyntaxException {
        log.debug("REST request to save Rifornimento : {}", rifornimento);
        if (rifornimento.getId() != null) {
            throw new BadRequestAlertException("A new rifornimento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rifornimento result = rifornimentoService.save(rifornimento);
        return ResponseEntity
            .created(new URI("/api/rifornimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rifornimentos} : Updates an existing rifornimento.
     *
     * @param rifornimento the rifornimento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rifornimento,
     * or with status {@code 400 (Bad Request)} if the rifornimento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rifornimento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rifornimentos")
    public ResponseEntity<Rifornimento> updateRifornimento(@Valid @RequestBody Rifornimento rifornimento) throws URISyntaxException {
        log.debug("REST request to update Rifornimento : {}", rifornimento);
        if (rifornimento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rifornimento result = rifornimentoService.save(rifornimento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rifornimento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rifornimentos} : Updates given fields of an existing rifornimento.
     *
     * @param rifornimento the rifornimento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rifornimento,
     * or with status {@code 400 (Bad Request)} if the rifornimento is not valid,
     * or with status {@code 404 (Not Found)} if the rifornimento is not found,
     * or with status {@code 500 (Internal Server Error)} if the rifornimento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rifornimentos", consumes = "application/merge-patch+json")
    public ResponseEntity<Rifornimento> partialUpdateRifornimento(@NotNull @RequestBody Rifornimento rifornimento)
        throws URISyntaxException {
        log.debug("REST request to update Rifornimento partially : {}", rifornimento);
        if (rifornimento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Rifornimento> result = rifornimentoService.partialUpdate(rifornimento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rifornimento.getId().toString())
        );
    }

    /**
     * {@code GET  /rifornimentos} : get all the rifornimentos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rifornimentos in body.
     */
    @GetMapping("/rifornimentos")
    public ResponseEntity<List<Rifornimento>> getAllRifornimentos(Pageable pageable) {
        log.debug("REST request to get a page of Rifornimentos");
        Page<Rifornimento> page = rifornimentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rifornimentos/:id} : get the "id" rifornimento.
     *
     * @param id the id of the rifornimento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rifornimento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rifornimentos/{id}")
    public ResponseEntity<Rifornimento> getRifornimento(@PathVariable Long id) {
        log.debug("REST request to get Rifornimento : {}", id);
        Optional<Rifornimento> rifornimento = rifornimentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rifornimento);
    }

    /**
     * {@code DELETE  /rifornimentos/:id} : delete the "id" rifornimento.
     *
     * @param id the id of the rifornimento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rifornimentos/{id}")
    public ResponseEntity<Void> deleteRifornimento(@PathVariable Long id) {
        log.debug("REST request to delete Rifornimento : {}", id);
        rifornimentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
