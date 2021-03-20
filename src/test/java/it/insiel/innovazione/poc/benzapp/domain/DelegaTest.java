package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DelegaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Delega.class);
        Delega delega1 = new Delega();
        delega1.setId(1L);
        Delega delega2 = new Delega();
        delega2.setId(delega1.getId());
        assertThat(delega1).isEqualTo(delega2);
        delega2.setId(2L);
        assertThat(delega1).isNotEqualTo(delega2);
        delega1.setId(null);
        assertThat(delega1).isNotEqualTo(delega2);
    }
}
