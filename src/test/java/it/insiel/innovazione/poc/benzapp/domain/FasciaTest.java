package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FasciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fascia.class);
        Fascia fascia1 = new Fascia();
        fascia1.setId(1L);
        Fascia fascia2 = new Fascia();
        fascia2.setId(fascia1.getId());
        assertThat(fascia1).isEqualTo(fascia2);
        fascia2.setId(2L);
        assertThat(fascia1).isNotEqualTo(fascia2);
        fascia1.setId(null);
        assertThat(fascia1).isNotEqualTo(fascia2);
    }
}
