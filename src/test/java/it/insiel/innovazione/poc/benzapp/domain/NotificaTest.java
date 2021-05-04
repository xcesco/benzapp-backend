package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notifica.class);
        Notifica notifica1 = new Notifica();
        notifica1.setId(1L);
        Notifica notifica2 = new Notifica();
        notifica2.setId(notifica1.getId());
        assertThat(notifica1).isEqualTo(notifica2);
        notifica2.setId(2L);
        assertThat(notifica1).isNotEqualTo(notifica2);
        notifica1.setId(null);
        assertThat(notifica1).isNotEqualTo(notifica2);
    }
}
