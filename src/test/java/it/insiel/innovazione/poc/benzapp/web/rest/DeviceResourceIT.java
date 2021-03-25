package it.insiel.innovazione.poc.benzapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.insiel.innovazione.poc.benzapp.IntegrationTest;
import it.insiel.innovazione.poc.benzapp.domain.Device;
import it.insiel.innovazione.poc.benzapp.repository.DeviceRepository;
import it.insiel.innovazione.poc.benzapp.service.DeviceQueryService;
import it.insiel.innovazione.poc.benzapp.service.dto.DeviceCriteria;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceQueryService deviceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device().owner(DEFAULT_OWNER).deviceId(DEFAULT_DEVICE_ID);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity(EntityManager em) {
        Device device = new Device().owner(UPDATED_OWNER).deviceId(UPDATED_DEVICE_ID);
        return device;
    }

    @BeforeEach
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();
        // Create the Device
        restDeviceMockMvc
            .perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testDevice.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);

        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setOwner(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeviceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setDeviceId(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post("/api/devices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get("/api/devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get("/api/devices/{id}", device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID));
    }

    @Test
    @Transactional
    void getDevicesByIdFiltering() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        Long id = device.getId();

        defaultDeviceShouldBeFound("id.equals=" + id);
        defaultDeviceShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDevicesByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where owner equals to DEFAULT_OWNER
        defaultDeviceShouldBeFound("owner.equals=" + DEFAULT_OWNER);

        // Get all the deviceList where owner equals to UPDATED_OWNER
        defaultDeviceShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllDevicesByOwnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where owner not equals to DEFAULT_OWNER
        defaultDeviceShouldNotBeFound("owner.notEquals=" + DEFAULT_OWNER);

        // Get all the deviceList where owner not equals to UPDATED_OWNER
        defaultDeviceShouldBeFound("owner.notEquals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllDevicesByOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where owner in DEFAULT_OWNER or UPDATED_OWNER
        defaultDeviceShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

        // Get all the deviceList where owner equals to UPDATED_OWNER
        defaultDeviceShouldNotBeFound("owner.in=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllDevicesByOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where owner is not null
        defaultDeviceShouldBeFound("owner.specified=true");

        // Get all the deviceList where owner is null
        defaultDeviceShouldNotBeFound("owner.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByOwnerContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where owner contains DEFAULT_OWNER
        defaultDeviceShouldBeFound("owner.contains=" + DEFAULT_OWNER);

        // Get all the deviceList where owner contains UPDATED_OWNER
        defaultDeviceShouldNotBeFound("owner.contains=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllDevicesByOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where owner does not contain DEFAULT_OWNER
        defaultDeviceShouldNotBeFound("owner.doesNotContain=" + DEFAULT_OWNER);

        // Get all the deviceList where owner does not contain UPDATED_OWNER
        defaultDeviceShouldBeFound("owner.doesNotContain=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceId equals to DEFAULT_DEVICE_ID
        defaultDeviceShouldBeFound("deviceId.equals=" + DEFAULT_DEVICE_ID);

        // Get all the deviceList where deviceId equals to UPDATED_DEVICE_ID
        defaultDeviceShouldNotBeFound("deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceId not equals to DEFAULT_DEVICE_ID
        defaultDeviceShouldNotBeFound("deviceId.notEquals=" + DEFAULT_DEVICE_ID);

        // Get all the deviceList where deviceId not equals to UPDATED_DEVICE_ID
        defaultDeviceShouldBeFound("deviceId.notEquals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceId in DEFAULT_DEVICE_ID or UPDATED_DEVICE_ID
        defaultDeviceShouldBeFound("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID);

        // Get all the deviceList where deviceId equals to UPDATED_DEVICE_ID
        defaultDeviceShouldNotBeFound("deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceId is not null
        defaultDeviceShouldBeFound("deviceId.specified=true");

        // Get all the deviceList where deviceId is null
        defaultDeviceShouldNotBeFound("deviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceIdContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceId contains DEFAULT_DEVICE_ID
        defaultDeviceShouldBeFound("deviceId.contains=" + DEFAULT_DEVICE_ID);

        // Get all the deviceList where deviceId contains UPDATED_DEVICE_ID
        defaultDeviceShouldNotBeFound("deviceId.contains=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllDevicesByDeviceIdNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceId does not contain DEFAULT_DEVICE_ID
        defaultDeviceShouldNotBeFound("deviceId.doesNotContain=" + DEFAULT_DEVICE_ID);

        // Get all the deviceList where deviceId does not contain UPDATED_DEVICE_ID
        defaultDeviceShouldBeFound("deviceId.doesNotContain=" + UPDATED_DEVICE_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceShouldBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get("/api/devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)));

        // Check, that the count call also returns 1
        restDeviceMockMvc
            .perform(get("/api/devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceShouldNotBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get("/api/devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceMockMvc
            .perform(get("/api/devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice.owner(UPDATED_OWNER).deviceId(UPDATED_DEVICE_ID);

        restDeviceMockMvc
            .perform(put("/api/devices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedDevice)))
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void updateNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put("/api/devices").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        restDeviceMockMvc
            .perform(
                patch("/api/devices")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testDevice.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice.owner(UPDATED_OWNER).deviceId(UPDATED_DEVICE_ID);

        restDeviceMockMvc
            .perform(
                patch("/api/devices")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void partialUpdateDeviceShouldThrown() throws Exception {
        // Update the device without id should throw
        Device partialUpdatedDevice = new Device();

        restDeviceMockMvc
            .perform(
                patch("/api/devices")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Delete the device
        restDeviceMockMvc
            .perform(delete("/api/devices/{id}", device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
