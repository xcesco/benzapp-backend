package it.insiel.innovazione.poc.benzapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link it.insiel.innovazione.poc.benzapp.domain.Notifica} entity.
 */
public class NotificaDTO implements Serializable {

    private Long id;

    @NotNull
    private String targa;

    @NotNull
    private ZonedDateTime data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificaDTO)) {
            return false;
        }

        NotificaDTO notificaDTO = (NotificaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificaDTO{" +
            "id=" + getId() +
            ", targa='" + getTarga() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
