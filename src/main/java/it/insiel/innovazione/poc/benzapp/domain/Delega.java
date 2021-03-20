package it.insiel.innovazione.poc.benzapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Delega.
 */
@Entity
@Table(name = "deleghe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Delega implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tesseras", "delegas" }, allowSetters = true)
    private Cittadino cittadino;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rifornimentos", "delegas", "cittadino" }, allowSetters = true)
    private Tessera tessera;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Delega id(Long id) {
        this.id = id;
        return this;
    }

    public Cittadino getCittadino() {
        return this.cittadino;
    }

    public Delega cittadino(Cittadino cittadino) {
        this.setCittadino(cittadino);
        return this;
    }

    public void setCittadino(Cittadino cittadino) {
        this.cittadino = cittadino;
    }

    public Tessera getTessera() {
        return this.tessera;
    }

    public Delega tessera(Tessera tessera) {
        this.setTessera(tessera);
        return this;
    }

    public void setTessera(Tessera tessera) {
        this.tessera = tessera;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Delega)) {
            return false;
        }
        return id != null && id.equals(((Delega) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Delega{" +
            "id=" + getId() +
            "}";
    }
}
