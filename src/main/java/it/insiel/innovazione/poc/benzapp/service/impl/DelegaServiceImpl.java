package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Delega;
import it.insiel.innovazione.poc.benzapp.repository.DelegaRepository;
import it.insiel.innovazione.poc.benzapp.service.DelegaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Delega}.
 */
@Service
@Transactional
public class DelegaServiceImpl implements DelegaService {

    private final Logger log = LoggerFactory.getLogger(DelegaServiceImpl.class);

    private final DelegaRepository delegaRepository;

    public DelegaServiceImpl(DelegaRepository delegaRepository) {
        this.delegaRepository = delegaRepository;
    }

    @Override
    public Delega save(Delega delega) {
        log.debug("Request to save Delega : {}", delega);
        return delegaRepository.save(delega);
    }

    @Override
    public Optional<Delega> partialUpdate(Delega delega) {
        log.debug("Request to partially update Delega : {}", delega);

        return delegaRepository
            .findById(delega.getId())
            .map(
                existingDelega -> {
                    return existingDelega;
                }
            )
            .map(delegaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Delega> findAll(Pageable pageable) {
        log.debug("Request to get all Delegas");
        return delegaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Delega> findOne(Long id) {
        log.debug("Request to get Delega : {}", id);
        return delegaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Delega : {}", id);
        delegaRepository.deleteById(id);
    }
}
