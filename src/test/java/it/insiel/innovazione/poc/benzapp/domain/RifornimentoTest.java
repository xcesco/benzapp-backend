package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RifornimentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rifornimento.class);
        Rifornimento rifornimento1 = new Rifornimento();
        rifornimento1.setId(1L);
        Rifornimento rifornimento2 = new Rifornimento();
        rifornimento2.setId(rifornimento1.getId());
        assertThat(rifornimento1).isEqualTo(rifornimento2);
        rifornimento2.setId(2L);
        assertThat(rifornimento1).isNotEqualTo(rifornimento2);
        rifornimento1.setId(null);
        assertThat(rifornimento1).isNotEqualTo(rifornimento2);
    }
}
