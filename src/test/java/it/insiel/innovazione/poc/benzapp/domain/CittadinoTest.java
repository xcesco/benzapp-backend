package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CittadinoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cittadino.class);
        Cittadino cittadino1 = new Cittadino();
        cittadino1.setId(1L);
        Cittadino cittadino2 = new Cittadino();
        cittadino2.setId(cittadino1.getId());
        assertThat(cittadino1).isEqualTo(cittadino2);
        cittadino2.setId(2L);
        assertThat(cittadino1).isNotEqualTo(cittadino2);
        cittadino1.setId(null);
        assertThat(cittadino1).isNotEqualTo(cittadino2);
    }
}
