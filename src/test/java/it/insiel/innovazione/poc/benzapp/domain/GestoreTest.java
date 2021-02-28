package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GestoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gestore.class);
        Gestore gestore1 = new Gestore();
        gestore1.setId(1L);
        Gestore gestore2 = new Gestore();
        gestore2.setId(gestore1.getId());
        assertThat(gestore1).isEqualTo(gestore2);
        gestore2.setId(2L);
        assertThat(gestore1).isNotEqualTo(gestore2);
        gestore1.setId(null);
        assertThat(gestore1).isNotEqualTo(gestore2);
    }
}
