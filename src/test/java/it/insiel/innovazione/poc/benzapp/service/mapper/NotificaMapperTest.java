package it.insiel.innovazione.poc.benzapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificaMapperTest {

    private NotificaMapper notificaMapper;

    @BeforeEach
    public void setUp() {
        notificaMapper = new NotificaMapperImpl();
    }
}
