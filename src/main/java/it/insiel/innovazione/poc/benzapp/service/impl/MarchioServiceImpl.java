package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Marchio;
import it.insiel.innovazione.poc.benzapp.repository.MarchioRepository;
import it.insiel.innovazione.poc.benzapp.service.MarchioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Marchio}.
 */
@Service
@Transactional
public class MarchioServiceImpl implements MarchioService {

    private final Logger log = LoggerFactory.getLogger(MarchioServiceImpl.class);

    private final MarchioRepository marchioRepository;

    public MarchioServiceImpl(MarchioRepository marchioRepository) {
        this.marchioRepository = marchioRepository;
    }

    @Override
    public Marchio save(Marchio marchio) {
        log.debug("Request to save Marchio : {}", marchio);
        return marchioRepository.save(marchio);
    }

    @Override
    public Optional<Marchio> partialUpdate(Marchio marchio) {
        log.debug("Request to partially update Marchio : {}", marchio);

        return marchioRepository
            .findById(marchio.getId())
            .map(
                existingMarchio -> {
                    if (marchio.getImmagine() != null) {
                        existingMarchio.setImmagine(marchio.getImmagine());
                    }
                    if (marchio.getImmagineContentType() != null) {
                        existingMarchio.setImmagineContentType(marchio.getImmagineContentType());
                    }

                    if (marchio.getNome() != null) {
                        existingMarchio.setNome(marchio.getNome());
                    }

                    return existingMarchio;
                }
            )
            .map(marchioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Marchio> findAll(Pageable pageable) {
        log.debug("Request to get all Marchios");
        return marchioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Marchio> findOne(Long id) {
        log.debug("Request to get Marchio : {}", id);
        return marchioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Marchio : {}", id);
        marchioRepository.deleteById(id);
    }
}
