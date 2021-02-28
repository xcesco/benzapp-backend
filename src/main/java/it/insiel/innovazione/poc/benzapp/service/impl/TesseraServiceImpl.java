package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import it.insiel.innovazione.poc.benzapp.repository.TesseraRepository;
import it.insiel.innovazione.poc.benzapp.service.TesseraService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tessera}.
 */
@Service
@Transactional
public class TesseraServiceImpl implements TesseraService {

    private final Logger log = LoggerFactory.getLogger(TesseraServiceImpl.class);

    private final TesseraRepository tesseraRepository;

    public TesseraServiceImpl(TesseraRepository tesseraRepository) {
        this.tesseraRepository = tesseraRepository;
    }

    @Override
    public Tessera save(Tessera tessera) {
        log.debug("Request to save Tessera : {}", tessera);
        return tesseraRepository.save(tessera);
    }

    @Override
    public Optional<Tessera> partialUpdate(Tessera tessera) {
        log.debug("Request to partially update Tessera : {}", tessera);

        return tesseraRepository
            .findById(tessera.getId())
            .map(
                existingTessera -> {
                    if (tessera.getCodice() != null) {
                        existingTessera.setCodice(tessera.getCodice());
                    }

                    if (tessera.getDataEmissione() != null) {
                        existingTessera.setDataEmissione(tessera.getDataEmissione());
                    }

                    if (tessera.getImmagine() != null) {
                        existingTessera.setImmagine(tessera.getImmagine());
                    }
                    if (tessera.getImmagineContentType() != null) {
                        existingTessera.setImmagineContentType(tessera.getImmagineContentType());
                    }

                    if (tessera.getTarga() != null) {
                        existingTessera.setTarga(tessera.getTarga());
                    }

                    if (tessera.getVeicolo() != null) {
                        existingTessera.setVeicolo(tessera.getVeicolo());
                    }

                    if (tessera.getCarburante() != null) {
                        existingTessera.setCarburante(tessera.getCarburante());
                    }

                    return existingTessera;
                }
            )
            .map(tesseraRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tessera> findAll(Pageable pageable) {
        log.debug("Request to get all Tesseras");
        return tesseraRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tessera> findOne(Long id) {
        log.debug("Request to get Tessera : {}", id);
        return tesseraRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tessera : {}", id);
        tesseraRepository.deleteById(id);
    }
}
