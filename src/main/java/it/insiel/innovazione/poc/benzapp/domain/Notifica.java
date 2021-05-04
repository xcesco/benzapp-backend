package it.insiel.innovazione.poc.benzapp.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notifica.
 */
@Entity
@Table(name = "notifica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notifica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "targa", nullable = false)
    private String targa;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notifica id(Long id) {
        this.id = id;
        return this;
    }

    public String getTarga() {
        return this.targa;
    }

    public Notifica targa(String targa) {
        this.targa = targa;
        return this;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public ZonedDateTime getData() {
        return this.data;
    }

    public Notifica data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notifica)) {
            return false;
        }
        return id != null && id.equals(((Notifica) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notifica{" +
            "id=" + getId() +
            ", targa='" + getTarga() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
