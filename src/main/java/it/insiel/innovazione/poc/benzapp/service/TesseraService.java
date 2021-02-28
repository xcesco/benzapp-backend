package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Tessera}.
 */
public interface TesseraService {
    /**
     * Save a tessera.
     *
     * @param tessera the entity to save.
     * @return the persisted entity.
     */
    Tessera save(Tessera tessera);

    /**
     * Partially updates a tessera.
     *
     * @param tessera the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tessera> partialUpdate(Tessera tessera);

    /**
     * Get all the tesseras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tessera> findAll(Pageable pageable);

    /**
     * Get the "id" tessera.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tessera> findOne(Long id);

    /**
     * Delete the "id" tessera.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
