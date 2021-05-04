package it.insiel.innovazione.poc.benzapp.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import it.insiel.innovazione.poc.benzapp.domain.Device;
import it.insiel.innovazione.poc.benzapp.domain.Notifica;
import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.fcm.FCMService;
import it.insiel.innovazione.poc.benzapp.fcm.PushNotificationRequest;
import it.insiel.innovazione.poc.benzapp.repository.DeviceRepository;
import it.insiel.innovazione.poc.benzapp.repository.NotificaRepository;
import it.insiel.innovazione.poc.benzapp.repository.RifornimentoRepository;
import it.insiel.innovazione.poc.benzapp.service.RifornimentoService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
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

    private final NotificaRepository notificaRepository;

    private final RifornimentoRepository rifornimentoRepository;

    private final DeviceRepository deviceRepository;

    private final FCMService fcmService;

    public RifornimentoServiceImpl(
        NotificaRepository notificaRepository,
        RifornimentoRepository rifornimentoRepository,
        DeviceRepository deviceRepository,
        FCMService fcmService
    ) {
        this.notificaRepository = notificaRepository;
        this.rifornimentoRepository = rifornimentoRepository;
        this.fcmService = fcmService;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Rifornimento save(Rifornimento rifornimento) {
        log.debug("Request to save Rifornimento : {}", rifornimento);
        Rifornimento result = rifornimentoRepository.save(rifornimento);

        if (rifornimento.getTessera() != null && rifornimento.getTessera().getCittadino() != null) {
            String targa = rifornimento.getTessera().getTarga();
            Notifica notifica = notificaRepository.findByTarga(targa).orElse(new Notifica());
            notifica.setTarga(targa);
            notifica.setData(ZonedDateTime.now());
            notificaRepository.save(notifica);

            List<Device> deviceList = this.deviceRepository.findAllByOwner(rifornimento.getTessera().getCittadino().getOwner());
            if (deviceList != null) {
                for (Device device : deviceList) {
                    PushNotificationRequest request = new PushNotificationRequest();
                    request.setMessage(
                        String.format(
                            "E' stato registrato il rifornimento di %.2f litri per il veicolo %s presso %s",
                            result.getLitriErogati(),
                            result.getTessera().getTarga(),
                            //                            result.getGestore().getMarchio().getNome(),
                            result.getGestore().getIndirizzo()
                        )
                    );
                    request.setToken(device.getDeviceId());
                    try {
                        fcmService.sendMessageToToken(request);
                    } catch (InterruptedException | ExecutionException e) {
                        if (e.getCause() instanceof FirebaseMessagingException) {
                            if ("registration-token-not-registered".equals(((FirebaseMessagingException) e.getCause()).getErrorCode())) {
                                deviceRepository.deleteById(device.getId());
                                log.warn("delete obsleted gcm token" + device.getId());
                            }
                        } else {
                            e.printStackTrace();
                        }
                        // cancella devi
                    }
                }
            }
        }
        return result;
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
