package it.insiel.innovazione.poc.benzapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fascia.
 */
@Entity
@Table(name = "fascia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fascia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "sconto_benzina")
    private Float scontoBenzina;

    @Column(name = "sconto_gasolio")
    private Float scontoGasolio;

    @OneToMany(mappedBy = "fascia")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "delegas", "tesseras", "fascia" }, allowSetters = true)
    private Set<Cittadino> cittadinos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fascia id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public Fascia descrizione(String descrizione) {
        this.descrizione = descrizione;
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Float getScontoBenzina() {
        return this.scontoBenzina;
    }

    public Fascia scontoBenzina(Float scontoBenzina) {
        this.scontoBenzina = scontoBenzina;
        return this;
    }

    public void setScontoBenzina(Float scontoBenzina) {
        this.scontoBenzina = scontoBenzina;
    }

    public Float getScontoGasolio() {
        return this.scontoGasolio;
    }

    public Fascia scontoGasolio(Float scontoGasolio) {
        this.scontoGasolio = scontoGasolio;
        return this;
    }

    public void setScontoGasolio(Float scontoGasolio) {
        this.scontoGasolio = scontoGasolio;
    }

    public Set<Cittadino> getCittadinos() {
        return this.cittadinos;
    }

    public Fascia cittadinos(Set<Cittadino> cittadinos) {
        this.setCittadinos(cittadinos);
        return this;
    }

    public Fascia addCittadino(Cittadino cittadino) {
        this.cittadinos.add(cittadino);
        cittadino.setFascia(this);
        return this;
    }

    public Fascia removeCittadino(Cittadino cittadino) {
        this.cittadinos.remove(cittadino);
        cittadino.setFascia(null);
        return this;
    }

    public void setCittadinos(Set<Cittadino> cittadinos) {
        if (this.cittadinos != null) {
            this.cittadinos.forEach(i -> i.setFascia(null));
        }
        if (cittadinos != null) {
            cittadinos.forEach(i -> i.setFascia(this));
        }
        this.cittadinos = cittadinos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fascia)) {
            return false;
        }
        return id != null && id.equals(((Fascia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fascia{" +
            "id=" + getId() +
            ", descrizione='" + getDescrizione() + "'" +
            ", scontoBenzina=" + getScontoBenzina() +
            ", scontoGasolio=" + getScontoGasolio() +
            "}";
    }
}
