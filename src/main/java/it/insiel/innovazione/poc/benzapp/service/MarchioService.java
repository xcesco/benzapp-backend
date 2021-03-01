package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.Marchio;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Marchio}.
 */
public interface MarchioService {
    /**
     * Save a marchio.
     *
     * @param marchio the entity to save.
     * @return the persisted entity.
     */
    Marchio save(Marchio marchio);

    /**
     * Partially updates a marchio.
     *
     * @param marchio the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Marchio> partialUpdate(Marchio marchio);

    /**
     * Get all the marchios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Marchio> findAll(Pageable pageable);

    /**
     * Get the "id" marchio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Marchio> findOne(Long id);

    /**
     * Delete the "id" marchio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
