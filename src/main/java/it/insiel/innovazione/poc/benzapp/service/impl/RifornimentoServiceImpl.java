package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.repository.RifornimentoRepository;
import it.insiel.innovazione.poc.benzapp.service.RifornimentoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rifornimento}.
 */
@Service
@Transactional
public class RifornimentoServiceImpl implements RifornimentoService {

    private final Logger log = LoggerFactory.getLogger(RifornimentoServiceImpl.class);

    private final RifornimentoRepository rifornimentoRepository;

    public RifornimentoServiceImpl(RifornimentoRepository rifornimentoRepository) {
        this.rifornimentoRepository = rifornimentoRepository;
    }

    @Override
    public Rifornimento save(Rifornimento rifornimento) {
        log.debug("Request to save Rifornimento : {}", rifornimento);
        return rifornimentoRepository.save(rifornimento);
    }

    @Override
    public Optional<Rifornimento> partialUpdate(Rifornimento rifornimento) {
        log.debug("Request to partially update Rifornimento : {}", rifornimento);

        return rifornimentoRepository
            .findById(rifornimento.getId())
            .map(
                existingRifornimento -> {
                    if (rifornimento.getData() != null) {
                        existingRifornimento.setData(rifornimento.getData());
                    }

                    if (rifornimento.getProgressivo() != null) {
                        existingRifornimento.setProgressivo(rifornimento.getProgressivo());
                    }

                    if (rifornimento.getLitriErogati() != null) {
                        existingRifornimento.setLitriErogati(rifornimento.getLitriErogati());
                    }

                    if (rifornimento.getSconto() != null) {
                        existingRifornimento.setSconto(rifornimento.getSconto());
                    }

                    if (rifornimento.getPrezzoAlLitro() != null) {
                        existingRifornimento.setPrezzoAlLitro(rifornimento.getPrezzoAlLitro());
                    }

                    if (rifornimento.getTipoCarburante() != null) {
                        existingRifornimento.setTipoCarburante(rifornimento.getTipoCarburante());
                    }

                    return existingRifornimento;
                }
            )
            .map(rifornimentoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rifornimento> findAll(Pageable pageable) {
        log.debug("Request to get all Rifornimentos");
        return rifornimentoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rifornimento> findOne(Long id) {
        log.debug("Request to get Rifornimento : {}", id);
        return rifornimentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rifornimento : {}", id);
        rifornimentoRepository.deleteById(id);
    }
}
