package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.Delega;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Delega}.
 */
public interface DelegaService {
    /**
     * Save a delega.
     *
     * @param delega the entity to save.
     * @return the persisted entity.
     */
    Delega save(Delega delega);

    /**
     * Partially updates a delega.
     *
     * @param delega the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Delega> partialUpdate(Delega delega);

    /**
     * Get all the delegas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Delega> findAll(Pageable pageable);

    /**
     * Get the "id" delega.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Delega> findOne(Long id);

    /**
     * Delete the "id" delega.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
