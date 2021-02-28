package it.insiel.innovazione.poc.benzapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.insiel.innovazione.poc.benzapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TesseraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tessera.class);
        Tessera tessera1 = new Tessera();
        tessera1.setId(1L);
        Tessera tessera2 = new Tessera();
        tessera2.setId(tessera1.getId());
        assertThat(tessera1).isEqualTo(tessera2);
        tessera2.setId(2L);
        assertThat(tessera1).isNotEqualTo(tessera2);
        tessera1.setId(null);
        assertThat(tessera1).isNotEqualTo(tessera2);
    }
}
