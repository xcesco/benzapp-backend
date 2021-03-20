package it.insiel.innovazione.poc.benzapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoVeicolo;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tessera.
 */
@Entity
@Table(name = "tessere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tessera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "codice", nullable = false)
    private String codice;

    @NotNull
    @Column(name = "data_emissione", nullable = false)
    private ZonedDateTime dataEmissione;

    @Lob
    @Column(name = "immagine")
    private byte[] immagine;

    @Column(name = "immagine_content_type")
    private String immagineContentType;

    @NotNull
    @Column(name = "targa", nullable = false)
    private String targa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "veicolo", nullable = false)
    private TipoVeicolo veicolo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "carburante", nullable = false)
    private TipoCarburante carburante;

    @OneToMany(mappedBy = "tessera")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gestore", "tessera" }, allowSetters = true)
    private Set<Rifornimento> rifornimentos = new HashSet<>();

    @OneToMany(mappedBy = "tessera")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cittadino", "tessera" }, allowSetters = true)
    private Set<Delega> delegas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tesseras", "delegas" }, allowSetters = true)
    private Cittadino cittadino;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tessera id(Long id) {
        this.id = id;
        return this;
    }

    public String getCodice() {
        return this.codice;
    }

    public Tessera codice(String codice) {
        this.codice = codice;
        return this;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public ZonedDateTime getDataEmissione() {
        return this.dataEmissione;
    }

    public Tessera dataEmissione(ZonedDateTime dataEmissione) {
        this.dataEmissione = dataEmissione;
        return this;
    }

    public void setDataEmissione(ZonedDateTime dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public byte[] getImmagine() {
        return this.immagine;
    }

    public Tessera immagine(byte[] immagine) {
        this.immagine = immagine;
        return this;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public String getImmagineContentType() {
        return this.immagineContentType;
    }

    public Tessera immagineContentType(String immagineContentType) {
        this.immagineContentType = immagineContentType;
        return this;
    }

    public void setImmagineContentType(String immagineContentType) {
        this.immagineContentType = immagineContentType;
    }

    public String getTarga() {
        return this.targa;
    }

    public Tessera targa(String targa) {
        this.targa = targa;
        return this;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public TipoVeicolo getVeicolo() {
        return this.veicolo;
    }

    public Tessera veicolo(TipoVeicolo veicolo) {
        this.veicolo = veicolo;
        return this;
    }

    public void setVeicolo(TipoVeicolo veicolo) {
        this.veicolo = veicolo;
    }

    public TipoCarburante getCarburante() {
        return this.carburante;
    }

    public Tessera carburante(TipoCarburante carburante) {
        this.carburante = carburante;
        return this;
    }

    public void setCarburante(TipoCarburante carburante) {
        this.carburante = carburante;
    }

    public Set<Rifornimento> getRifornimentos() {
        return this.rifornimentos;
    }

    public Tessera rifornimentos(Set<Rifornimento> rifornimentos) {
        this.setRifornimentos(rifornimentos);
        return this;
    }

    public Tessera addRifornimento(Rifornimento rifornimento) {
        this.rifornimentos.add(rifornimento);
        rifornimento.setTessera(this);
        return this;
    }

    public Tessera removeRifornimento(Rifornimento rifornimento) {
        this.rifornimentos.remove(rifornimento);
        rifornimento.setTessera(null);
        return this;
    }

    public void setRifornimentos(Set<Rifornimento> rifornimentos) {
        if (this.rifornimentos != null) {
            this.rifornimentos.forEach(i -> i.setTessera(null));
        }
        if (rifornimentos != null) {
            rifornimentos.forEach(i -> i.setTessera(this));
        }
        this.rifornimentos = rifornimentos;
    }

    public Set<Delega> getDelegas() {
        return this.delegas;
    }

    public Tessera delegas(Set<Delega> delegas) {
        this.setDelegas(delegas);
        return this;
    }

    public Tessera addDelega(Delega delega) {
        this.delegas.add(delega);
        delega.setTessera(this);
        return this;
    }

    public Tessera removeDelega(Delega delega) {
        this.delegas.remove(delega);
        delega.setTessera(null);
        return this;
    }

    public void setDelegas(Set<Delega> delegas) {
        if (this.delegas != null) {
            this.delegas.forEach(i -> i.setTessera(null));
        }
        if (delegas != null) {
            delegas.forEach(i -> i.setTessera(this));
        }
        this.delegas = delegas;
    }

    public Cittadino getCittadino() {
        return this.cittadino;
    }

    public Tessera cittadino(Cittadino cittadino) {
        this.setCittadino(cittadino);
        return this;
    }

    public void setCittadino(Cittadino cittadino) {
        this.cittadino = cittadino;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tessera)) {
            return false;
        }
        return id != null && id.equals(((Tessera) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tessera{" +
            "id=" + getId() +
            ", codice='" + getCodice() + "'" +
            ", dataEmissione='" + getDataEmissione() + "'" +
            ", immagine='" + getImmagine() + "'" +
            ", immagineContentType='" + getImmagineContentType() + "'" +
            ", targa='" + getTarga() + "'" +
            ", veicolo='" + getVeicolo() + "'" +
            ", carburante='" + getCarburante() + "'" +
            "}";
    }
}
