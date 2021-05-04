package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Notifica;
import it.insiel.innovazione.poc.benzapp.repository.NotificaRepository;
import it.insiel.innovazione.poc.benzapp.service.NotificaService;
import it.insiel.innovazione.poc.benzapp.service.dto.NotificaDTO;
import it.insiel.innovazione.poc.benzapp.service.mapper.NotificaMapper;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Notifica}.
 */
@Service
@Transactional
public class NotificaServiceImpl implements NotificaService {

    private final Logger log = LoggerFactory.getLogger(NotificaServiceImpl.class);

    private final NotificaRepository notificaRepository;

    private final NotificaMapper notificaMapper;

    public NotificaServiceImpl(NotificaRepository notificaRepository, NotificaMapper notificaMapper) {
        this.notificaRepository = notificaRepository;
        this.notificaMapper = notificaMapper;
    }

    @Override
    public NotificaDTO save(NotificaDTO notificaDTO) {
        log.debug("Request to save Notifica : {}", notificaDTO);
        Notifica notifica = notificaMapper.toEntity(notificaDTO);
        notifica = notificaRepository.save(notifica);
        return notificaMapper.toDto(notifica);
    }

    @Override
    public Optional<NotificaDTO> partialUpdate(NotificaDTO notificaDTO) {
        log.debug("Request to partially update Notifica : {}", notificaDTO);

        return notificaRepository
            .findById(notificaDTO.getId())
            .map(
                existingNotifica -> {
                    notificaMapper.partialUpdate(existingNotifica, notificaDTO);
                    return existingNotifica;
                }
            )
            .map(notificaRepository::save)
            .map(notificaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificaDTO> findAll(Pageable pageable) {
        log.debug(String.format("Request to get all Notificas %s"));
        return notificaRepository.findAll(pageable).map(notificaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificaDTO> findOne(Long id) {
        log.debug("Request to get Notifica : {}", id);
        return notificaRepository.findById(id).map(notificaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notifica : {}", id);
        notificaRepository.deleteById(id);
    }
}
