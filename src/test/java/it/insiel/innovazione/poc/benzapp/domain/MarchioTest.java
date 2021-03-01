package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MarchioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Marchio.class);
        Marchio marchio1 = new Marchio();
        marchio1.setId(1L);
        Marchio marchio2 = new Marchio();
        marchio2.setId(marchio1.getId());
        assertThat(marchio1).isEqualTo(marchio2);
        marchio2.setId(2L);
        assertThat(marchio1).isNotEqualTo(marchio2);
        marchio1.setId(null);
        assertThat(marchio1).isNotEqualTo(marchio2);
    }
}
