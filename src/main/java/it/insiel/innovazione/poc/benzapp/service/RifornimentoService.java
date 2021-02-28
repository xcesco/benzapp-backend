package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Rifornimento}.
 */
public interface RifornimentoService {
    /**
     * Save a rifornimento.
     *
     * @param rifornimento the entity to save.
     * @return the persisted entity.
     */
    Rifornimento save(Rifornimento rifornimento);

    /**
     * Partially updates a rifornimento.
     *
     * @param rifornimento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Rifornimento> partialUpdate(Rifornimento rifornimento);

    /**
     * Get all the rifornimentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Rifornimento> findAll(Pageable pageable);

    /**
     * Get the "id" rifornimento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rifornimento> findOne(Long id);

    /**
     * Delete the "id" rifornimento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
