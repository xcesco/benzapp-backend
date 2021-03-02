package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Fascia;
import it.insiel.innovazione.poc.benzapp.repository.FasciaRepository;
import it.insiel.innovazione.poc.benzapp.service.FasciaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fascia}.
 */
@Service
@Transactional
public class FasciaServiceImpl implements FasciaService {

    private final Logger log = LoggerFactory.getLogger(FasciaServiceImpl.class);

    private final FasciaRepository fasciaRepository;

    public FasciaServiceImpl(FasciaRepository fasciaRepository) {
        this.fasciaRepository = fasciaRepository;
    }

    @Override
    public Fascia save(Fascia fascia) {
        log.debug("Request to save Fascia : {}", fascia);
        return fasciaRepository.save(fascia);
    }

    @Override
    public Optional<Fascia> partialUpdate(Fascia fascia) {
        log.debug("Request to partially update Fascia : {}", fascia);

        return fasciaRepository
            .findById(fascia.getId())
            .map(
                existingFascia -> {
                    if (fascia.getDescrizione() != null) {
                        existingFascia.setDescrizione(fascia.getDescrizione());
                    }

                    if (fascia.getScontoBenzina() != null) {
                        existingFascia.setScontoBenzina(fascia.getScontoBenzina());
                    }

                    if (fascia.getScontoGasolio() != null) {
                        existingFascia.setScontoGasolio(fascia.getScontoGasolio());
                    }

                    return existingFascia;
                }
            )
            .map(fasciaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fascia> findAll() {
        log.debug("Request to get all Fascias");
        return fasciaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fascia> findOne(Long id) {
        log.debug("Request to get Fascia : {}", id);
        return fasciaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fascia : {}", id);
        fasciaRepository.deleteById(id);
    }
}
