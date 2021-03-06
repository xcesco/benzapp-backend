package it.insiel.innovazione.poc.benzapp.service.impl;

import it.insiel.innovazione.poc.benzapp.domain.Device;
import it.insiel.innovazione.poc.benzapp.repository.DeviceRepository;
import it.insiel.innovazione.poc.benzapp.service.DeviceService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Device}.
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device save(Device device) {
        log.debug("Request to save Device : {}", device);

        Optional<Device> stored = deviceRepository.findDeviceByDeviceId(device.getDeviceId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (stored.isPresent()) {
            device = stored.get();
        }
        device.setOwner(authentication.getName());
        return deviceRepository.save(device);
    }

    @Override
    public Optional<Device> partialUpdate(Device device) {
        log.debug("Request to partially update Device : {}", device);

        return deviceRepository
            .findById(device.getId())
            .map(
                existingDevice -> {
                    if (device.getOwner() != null) {
                        existingDevice.setOwner(device.getOwner());
                    }

                    if (device.getDeviceId() != null) {
                        existingDevice.setDeviceId(device.getDeviceId());
                    }

                    return existingDevice;
                }
            )
            .map(deviceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Device> findOne(Long id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}
