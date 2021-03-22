package it.insiel.innovazione.poc.benzapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Rifornimento.
 */
@Entity
@Table(name = "rifornimenti")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rifornimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data", nullable = false)
    private ZonedDateTime data;

    @NotNull
    @Column(name = "litri_erogati", nullable = false)
    private Float litriErogati;

    @NotNull
    @Column(name = "sconto", nullable = false)
    private Float sconto;

    @NotNull
    @Column(name = "prezzo_al_litro", nullable = false)
    private Float prezzoAlLitro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_carburante", nullable = false)
    private TipoCarburante tipoCarburante;

    @ManyToOne
    @JsonIgnoreProperties(value = { "rifornimentos", "marchio" }, allowSetters = true)
    private Gestore gestore;

    @ManyToOne
    @JsonIgnoreProperties(value = { "delegas", "rifornimentos", "cittadino" }, allowSetters = true)
    private Tessera tessera;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rifornimento id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getData() {
        return this.data;
    }

    public Rifornimento data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public Float getLitriErogati() {
        return this.litriErogati;
    }

    public Rifornimento litriErogati(Float litriErogati) {
        this.litriErogati = litriErogati;
        return this;
    }

    public void setLitriErogati(Float litriErogati) {
        this.litriErogati = litriErogati;
    }

    public Float getSconto() {
        return this.sconto;
    }

    public Rifornimento sconto(Float sconto) {
        this.sconto = sconto;
        return this;
    }

    public void setSconto(Float sconto) {
        this.sconto = sconto;
    }

    public Float getPrezzoAlLitro() {
        return this.prezzoAlLitro;
    }

    public Rifornimento prezzoAlLitro(Float prezzoAlLitro) {
        this.prezzoAlLitro = prezzoAlLitro;
        return this;
    }

    public void setPrezzoAlLitro(Float prezzoAlLitro) {
        this.prezzoAlLitro = prezzoAlLitro;
    }

    public TipoCarburante getTipoCarburante() {
        return this.tipoCarburante;
    }

    public Rifornimento tipoCarburante(TipoCarburante tipoCarburante) {
        this.tipoCarburante = tipoCarburante;
        return this;
    }

    public void setTipoCarburante(TipoCarburante tipoCarburante) {
        this.tipoCarburante = tipoCarburante;
    }

    public Gestore getGestore() {
        return this.gestore;
    }

    public Rifornimento gestore(Gestore gestore) {
        this.setGestore(gestore);
        return this;
    }

    public void setGestore(Gestore gestore) {
        this.gestore = gestore;
    }

    public Tessera getTessera() {
        return this.tessera;
    }

    public Rifornimento tessera(Tessera tessera) {
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
        if (!(o instanceof Rifornimento)) {
            return false;
        }
        return id != null && id.equals(((Rifornimento) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rifornimento{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", litriErogati=" + getLitriErogati() +
            ", sconto=" + getSconto() +
            ", prezzoAlLitro=" + getPrezzoAlLitro() +
            ", tipoCarburante='" + getTipoCarburante() + "'" +
            "}";
    }
}
