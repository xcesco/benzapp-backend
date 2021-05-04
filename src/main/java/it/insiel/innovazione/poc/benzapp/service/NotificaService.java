package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.service.dto.NotificaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link it.insiel.innovazione.poc.benzapp.domain.Notifica}.
 */
public interface NotificaService {
    /**
     * Save a notifica.
     *
     * @param notificaDTO the entity to save.
     * @return the persisted entity.
     */
    NotificaDTO save(NotificaDTO notificaDTO);

    /**
     * Partially updates a notifica.
     *
     * @param notificaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificaDTO> partialUpdate(NotificaDTO notificaDTO);

    /**
     * Get all the notificas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" notifica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificaDTO> findOne(Long id);

    /**
     * Delete the "id" notifica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
