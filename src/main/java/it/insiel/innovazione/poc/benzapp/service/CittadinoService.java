package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Cittadino}.
 */
public interface CittadinoService {
    /**
     * Save a cittadino.
     *
     * @param cittadino the entity to save.
     * @return the persisted entity.
     */
    Cittadino save(Cittadino cittadino);

    /**
     * Partially updates a cittadino.
     *
     * @param cittadino the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cittadino> partialUpdate(Cittadino cittadino);

    /**
     * Get all the cittadinos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Cittadino> findAll(Pageable pageable);

    /**
     * Get the "id" cittadino.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cittadino> findOne(Long id);

    /**
     * Delete the "id" cittadino.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
