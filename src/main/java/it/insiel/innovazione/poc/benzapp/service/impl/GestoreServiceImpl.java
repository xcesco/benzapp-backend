package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Gestore;
import it.insiel.innovazione.poc.benzapp.repository.GestoreRepository;
import it.insiel.innovazione.poc.benzapp.service.GestoreService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gestore}.
 */
@Service
@Transactional
public class GestoreServiceImpl implements GestoreService {

    private final Logger log = LoggerFactory.getLogger(GestoreServiceImpl.class);

    private final GestoreRepository gestoreRepository;

    public GestoreServiceImpl(GestoreRepository gestoreRepository) {
        this.gestoreRepository = gestoreRepository;
    }

    @Override
    public Gestore save(Gestore gestore) {
        log.debug("Request to save Gestore : {}", gestore);
        return gestoreRepository.save(gestore);
    }

    @Override
    public Optional<Gestore> partialUpdate(Gestore gestore) {
        log.debug("Request to partially update Gestore : {}", gestore);

        return gestoreRepository
            .findById(gestore.getId())
            .map(
                existingGestore -> {
                    if (gestore.getProvincia() != null) {
                        existingGestore.setProvincia(gestore.getProvincia());
                    }

                    if (gestore.getComune() != null) {
                        existingGestore.setComune(gestore.getComune());
                    }

                    if (gestore.getIndirizzo() != null) {
                        existingGestore.setIndirizzo(gestore.getIndirizzo());
                    }

                    if (gestore.getLongitudine() != null) {
                        existingGestore.setLongitudine(gestore.getLongitudine());
                    }

                    if (gestore.getLatitudine() != null) {
                        existingGestore.setLatitudine(gestore.getLatitudine());
                    }

                    if (gestore.getTipo() != null) {
                        existingGestore.setTipo(gestore.getTipo());
                    }

                    if (gestore.getBenzinaPrezzoAlLitro() != null) {
                        existingGestore.setBenzinaPrezzoAlLitro(gestore.getBenzinaPrezzoAlLitro());
                    }

                    if (gestore.getGasolioPrezzoAlLitro() != null) {
                        existingGestore.setGasolioPrezzoAlLitro(gestore.getGasolioPrezzoAlLitro());
                    }

                    if (gestore.getOwner() != null) {
                        existingGestore.setOwner(gestore.getOwner());
                    }

                    return existingGestore;
                }
            )
            .map(gestoreRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Gestore> findAll(Pageable pageable) {
        log.debug("Request to get all Gestores");
        return gestoreRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Gestore> findOne(Long id) {
        log.debug("Request to get Gestore : {}", id);
        return gestoreRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gestore : {}", id);
        gestoreRepository.deleteById(id);
    }
}
