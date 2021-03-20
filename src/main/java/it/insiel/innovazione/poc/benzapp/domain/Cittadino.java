package it.insiel.innovazione.poc.benzapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cittadino.
 */
@Entity
@Table(name = "cittadini")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cittadino implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "codice_fiscale")
    private String codiceFiscale;

    @OneToMany(mappedBy = "cittadino")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rifornimentos", "delegas", "cittadino" }, allowSetters = true)
    private Set<Tessera> tesseras = new HashSet<>();

    @OneToMany(mappedBy = "cittadino")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cittadino", "tessera" }, allowSetters = true)
    private Set<Delega> delegas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cittadino id(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return this.nome;
    }

    public Cittadino nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public Cittadino cognome(String cognome) {
        this.cognome = cognome;
        return this;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceFiscale() {
        return this.codiceFiscale;
    }

    public Cittadino codiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
        return this;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public Set<Tessera> getTesseras() {
        return this.tesseras;
    }

    public Cittadino tesseras(Set<Tessera> tesseras) {
        this.setTesseras(tesseras);
        return this;
    }

    public Cittadino addTessera(Tessera tessera) {
        this.tesseras.add(tessera);
        tessera.setCittadino(this);
        return this;
    }

    public Cittadino removeTessera(Tessera tessera) {
        this.tesseras.remove(tessera);
        tessera.setCittadino(null);
        return this;
    }

    public void setTesseras(Set<Tessera> tesseras) {
        if (this.tesseras != null) {
            this.tesseras.forEach(i -> i.setCittadino(null));
        }
        if (tesseras != null) {
            tesseras.forEach(i -> i.setCittadino(this));
        }
        this.tesseras = tesseras;
    }

    public Set<Delega> getDelegas() {
        return this.delegas;
    }

    public Cittadino delegas(Set<Delega> delegas) {
        this.setDelegas(delegas);
        return this;
    }

    public Cittadino addDelega(Delega delega) {
        this.delegas.add(delega);
        delega.setCittadino(this);
        return this;
    }

    public Cittadino removeDelega(Delega delega) {
        this.delegas.remove(delega);
        delega.setCittadino(null);
        return this;
    }

    public void setDelegas(Set<Delega> delegas) {
        if (this.delegas != null) {
            this.delegas.forEach(i -> i.setCittadino(null));
        }
        if (delegas != null) {
            delegas.forEach(i -> i.setCittadino(this));
        }
        this.delegas = delegas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cittadino)) {
            return false;
        }
        return id != null && id.equals(((Cittadino) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cittadino{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cognome='" + getCognome() + "'" +
            ", codiceFiscale='" + getCodiceFiscale() + "'" +
            "}";
    }
}
