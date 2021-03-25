package it.insiel.innovazione.poc.benzapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoImpianto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Gestore.
 */
@Entity
@Table(name = "gestori")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Gestore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "comune")
    private String comune;

    @Column(name = "indirizzo")
    private String indirizzo;

    @Column(name = "longitudine")
    private Float longitudine;

    @Column(name = "latitudine")
    private Float latitudine;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoImpianto tipo;

    @Column(name = "benzina_prezzo_al_litro")
    private Float benzinaPrezzoAlLitro;

    @Column(name = "gasolio_prezzo_al_litro")
    private Float gasolioPrezzoAlLitro;

    @Column(name = "owner")
    private String owner;

    @OneToMany(mappedBy = "gestore")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gestore", "tessera" }, allowSetters = true)
    private Set<Rifornimento> rifornimentos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "gestores" }, allowSetters = true)
    private Marchio marchio;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gestore id(Long id) {
        this.id = id;
        return this;
    }

    public String getProvincia() {
        return this.provincia;
    }

    public Gestore provincia(String provincia) {
        this.provincia = provincia;
        return this;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getComune() {
        return this.comune;
    }

    public Gestore comune(String comune) {
        this.comune = comune;
        return this;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getIndirizzo() {
        return this.indirizzo;
    }

    public Gestore indirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
        return this;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Float getLongitudine() {
        return this.longitudine;
    }

    public Gestore longitudine(Float longitudine) {
        this.longitudine = longitudine;
        return this;
    }

    public void setLongitudine(Float longitudine) {
        this.longitudine = longitudine;
    }

    public Float getLatitudine() {
        return this.latitudine;
    }

    public Gestore latitudine(Float latitudine) {
        this.latitudine = latitudine;
        return this;
    }

    public void setLatitudine(Float latitudine) {
        this.latitudine = latitudine;
    }

    public TipoImpianto getTipo() {
        return this.tipo;
    }

    public Gestore tipo(TipoImpianto tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoImpianto tipo) {
        this.tipo = tipo;
    }

    public Float getBenzinaPrezzoAlLitro() {
        return this.benzinaPrezzoAlLitro;
    }

    public Gestore benzinaPrezzoAlLitro(Float benzinaPrezzoAlLitro) {
        this.benzinaPrezzoAlLitro = benzinaPrezzoAlLitro;
        return this;
    }

    public void setBenzinaPrezzoAlLitro(Float benzinaPrezzoAlLitro) {
        this.benzinaPrezzoAlLitro = benzinaPrezzoAlLitro;
    }

    public Float getGasolioPrezzoAlLitro() {
        return this.gasolioPrezzoAlLitro;
    }

    public Gestore gasolioPrezzoAlLitro(Float gasolioPrezzoAlLitro) {
        this.gasolioPrezzoAlLitro = gasolioPrezzoAlLitro;
        return this;
    }

    public void setGasolioPrezzoAlLitro(Float gasolioPrezzoAlLitro) {
        this.gasolioPrezzoAlLitro = gasolioPrezzoAlLitro;
    }

    public String getOwner() {
        return this.owner;
    }

    public Gestore owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<Rifornimento> getRifornimentos() {
        return this.rifornimentos;
    }

    public Gestore rifornimentos(Set<Rifornimento> rifornimentos) {
        this.setRifornimentos(rifornimentos);
        return this;
    }

    public Gestore addRifornimento(Rifornimento rifornimento) {
        this.rifornimentos.add(rifornimento);
        rifornimento.setGestore(this);
        return this;
    }

    public Gestore removeRifornimento(Rifornimento rifornimento) {
        this.rifornimentos.remove(rifornimento);
        rifornimento.setGestore(null);
        return this;
    }

    public void setRifornimentos(Set<Rifornimento> rifornimentos) {
        if (this.rifornimentos != null) {
            this.rifornimentos.forEach(i -> i.setGestore(null));
        }
        if (rifornimentos != null) {
            rifornimentos.forEach(i -> i.setGestore(this));
        }
        this.rifornimentos = rifornimentos;
    }

    public Marchio getMarchio() {
        return this.marchio;
    }

    public Gestore marchio(Marchio marchio) {
        this.setMarchio(marchio);
        return this;
    }

    public void setMarchio(Marchio marchio) {
        this.marchio = marchio;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gestore)) {
            return false;
        }
        return id != null && id.equals(((Gestore) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gestore{" +
            "id=" + getId() +
            ", provincia='" + getProvincia() + "'" +
            ", comune='" + getComune() + "'" +
            ", indirizzo='" + getIndirizzo() + "'" +
            ", longitudine=" + getLongitudine() +
            ", latitudine=" + getLatitudine() +
            ", tipo='" + getTipo() + "'" +
            ", benzinaPrezzoAlLitro=" + getBenzinaPrezzoAlLitro() +
            ", gasolioPrezzoAlLitro=" + getGasolioPrezzoAlLitro() +
            ", owner='" + getOwner() + "'" +
            "}";
    }
}
