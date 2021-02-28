package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import it.insiel.innovazione.poc.benzapp.repository.CittadinoRepository;
import it.insiel.innovazione.poc.benzapp.service.CittadinoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cittadino}.
 */
@Service
@Transactional
public class CittadinoServiceImpl implements CittadinoService {

    private final Logger log = LoggerFactory.getLogger(CittadinoServiceImpl.class);

    private final CittadinoRepository cittadinoRepository;

    public CittadinoServiceImpl(CittadinoRepository cittadinoRepository) {
        this.cittadinoRepository = cittadinoRepository;
    }

    @Override
    public Cittadino save(Cittadino cittadino) {
        log.debug("Request to save Cittadino : {}", cittadino);
        return cittadinoRepository.save(cittadino);
    }

    @Override
    public Optional<Cittadino> partialUpdate(Cittadino cittadino) {
        log.debug("Request to partially update Cittadino : {}", cittadino);

        return cittadinoRepository
            .findById(cittadino.getId())
            .map(
                existingCittadino -> {
                    if (cittadino.getNome() != null) {
                        existingCittadino.setNome(cittadino.getNome());
                    }

                    if (cittadino.getCognome() != null) {
                        existingCittadino.setCognome(cittadino.getCognome());
                    }

                    if (cittadino.getCodiceFiscale() != null) {
                        existingCittadino.setCodiceFiscale(cittadino.getCodiceFiscale());
                    }

                    return existingCittadino;
                }
            )
            .map(cittadinoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cittadino> findAll(Pageable pageable) {
        log.debug("Request to get all Cittadinos");
        return cittadinoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cittadino> findOne(Long id) {
        log.debug("Request to get Cittadino : {}", id);
        return cittadinoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cittadino : {}", id);
        cittadinoRepository.deleteById(id);
    }
}
