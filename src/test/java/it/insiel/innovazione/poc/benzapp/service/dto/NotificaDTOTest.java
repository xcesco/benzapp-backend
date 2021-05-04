package it.insiel.innovazione.poc.benzapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificaDTO.class);
        NotificaDTO notificaDTO1 = new NotificaDTO();
        notificaDTO1.setId(1L);
        NotificaDTO notificaDTO2 = new NotificaDTO();
        assertThat(notificaDTO1).isNotEqualTo(notificaDTO2);
        notificaDTO2.setId(notificaDTO1.getId());
        assertThat(notificaDTO1).isEqualTo(notificaDTO2);
        notificaDTO2.setId(2L);
        assertThat(notificaDTO1).isNotEqualTo(notificaDTO2);
        notificaDTO1.setId(null);
        assertThat(notificaDTO1).isNotEqualTo(notificaDTO2);
    }
}
