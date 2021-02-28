package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.Gestore;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Gestore}.
 */
public interface GestoreService {
    /**
     * Save a gestore.
     *
     * @param gestore the entity to save.
     * @return the persisted entity.
     */
    Gestore save(Gestore gestore);

    /**
     * Partially updates a gestore.
     *
     * @param gestore the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Gestore> partialUpdate(Gestore gestore);

    /**
     * Get all the gestores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Gestore> findAll(Pageable pageable);

    /**
     * Get the "id" gestore.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Gestore> findOne(Long id);

    /**
     * Delete the "id" gestore.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
