package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.Fascia;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Fascia}.
 */
public interface FasciaService {
    /**
     * Save a fascia.
     *
     * @param fascia the entity to save.
     * @return the persisted entity.
     */
    Fascia save(Fascia fascia);

    /**
     * Partially updates a fascia.
     *
     * @param fascia the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Fascia> partialUpdate(Fascia fascia);

    /**
     * Get all the fascias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Fascia> findAll(Pageable pageable);

    /**
     * Get the "id" fascia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Fascia> findOne(Long id);

    /**
     * Delete the "id" fascia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
