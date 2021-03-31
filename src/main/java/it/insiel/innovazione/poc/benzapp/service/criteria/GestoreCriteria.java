package it.insiel.innovazione.poc.benzapp.service.criteria;

import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoImpianto;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link it.insiel.innovazione.poc.benzapp.domain.Gestore} entity. This class is used
 * in {@link it.insiel.innovazione.poc.benzapp.web.rest.GestoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gestores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GestoreCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoImpianto
     */
    public static class TipoImpiantoFilter extends Filter<TipoImpianto> {

        public TipoImpiantoFilter() {}

        public TipoImpiantoFilter(TipoImpiantoFilter filter) {
            super(filter);
        }

        @Override
        public TipoImpiantoFilter copy() {
            return new TipoImpiantoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter provincia;

    private StringFilter comune;

    private StringFilter indirizzo;

    private FloatFilter longitudine;

    private FloatFilter latitudine;

    private TipoImpiantoFilter tipo;

    private FloatFilter benzinaPrezzoAlLitro;

    private FloatFilter gasolioPrezzoAlLitro;

    private StringFilter owner;

    private LongFilter rifornimentoId;

    private LongFilter marchioId;

    public GestoreCriteria() {}

    public GestoreCriteria(GestoreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.provincia = other.provincia == null ? null : other.provincia.copy();
        this.comune = other.comune == null ? null : other.comune.copy();
        this.indirizzo = other.indirizzo == null ? null : other.indirizzo.copy();
        this.longitudine = other.longitudine == null ? null : other.longitudine.copy();
        this.latitudine = other.latitudine == null ? null : other.latitudine.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.benzinaPrezzoAlLitro = other.benzinaPrezzoAlLitro == null ? null : other.benzinaPrezzoAlLitro.copy();
        this.gasolioPrezzoAlLitro = other.gasolioPrezzoAlLitro == null ? null : other.gasolioPrezzoAlLitro.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.rifornimentoId = other.rifornimentoId == null ? null : other.rifornimentoId.copy();
        this.marchioId = other.marchioId == null ? null : other.marchioId.copy();
    }

    @Override
    public GestoreCriteria copy() {
        return new GestoreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProvincia() {
        return provincia;
    }

    public StringFilter provincia() {
        if (provincia == null) {
            provincia = new StringFilter();
        }
        return provincia;
    }

    public void setProvincia(StringFilter provincia) {
        this.provincia = provincia;
    }

    public StringFilter getComune() {
        return comune;
    }

    public StringFilter comune() {
        if (comune == null) {
            comune = new StringFilter();
        }
        return comune;
    }

    public void setComune(StringFilter comune) {
        this.comune = comune;
    }

    public StringFilter getIndirizzo() {
        return indirizzo;
    }

    public StringFilter indirizzo() {
        if (indirizzo == null) {
            indirizzo = new StringFilter();
        }
        return indirizzo;
    }

    public void setIndirizzo(StringFilter indirizzo) {
        this.indirizzo = indirizzo;
    }

    public FloatFilter getLongitudine() {
        return longitudine;
    }

    public FloatFilter longitudine() {
        if (longitudine == null) {
            longitudine = new FloatFilter();
        }
        return longitudine;
    }

    public void setLongitudine(FloatFilter longitudine) {
        this.longitudine = longitudine;
    }

    public FloatFilter getLatitudine() {
        return latitudine;
    }

    public FloatFilter latitudine() {
        if (latitudine == null) {
            latitudine = new FloatFilter();
        }
        return latitudine;
    }

    public void setLatitudine(FloatFilter latitudine) {
        this.latitudine = latitudine;
    }

    public TipoImpiantoFilter getTipo() {
        return tipo;
    }

    public TipoImpiantoFilter tipo() {
        if (tipo == null) {
            tipo = new TipoImpiantoFilter();
        }
        return tipo;
    }

    public void setTipo(TipoImpiantoFilter tipo) {
        this.tipo = tipo;
    }

    public FloatFilter getBenzinaPrezzoAlLitro() {
        return benzinaPrezzoAlLitro;
    }

    public FloatFilter benzinaPrezzoAlLitro() {
        if (benzinaPrezzoAlLitro == null) {
            benzinaPrezzoAlLitro = new FloatFilter();
        }
        return benzinaPrezzoAlLitro;
    }

    public void setBenzinaPrezzoAlLitro(FloatFilter benzinaPrezzoAlLitro) {
        this.benzinaPrezzoAlLitro = benzinaPrezzoAlLitro;
    }

    public FloatFilter getGasolioPrezzoAlLitro() {
        return gasolioPrezzoAlLitro;
    }

    public FloatFilter gasolioPrezzoAlLitro() {
        if (gasolioPrezzoAlLitro == null) {
            gasolioPrezzoAlLitro = new FloatFilter();
        }
        return gasolioPrezzoAlLitro;
    }

    public void setGasolioPrezzoAlLitro(FloatFilter gasolioPrezzoAlLitro) {
        this.gasolioPrezzoAlLitro = gasolioPrezzoAlLitro;
    }

    public StringFilter getOwner() {
        return owner;
    }

    public StringFilter owner() {
        if (owner == null) {
            owner = new StringFilter();
        }
        return owner;
    }

    public void setOwner(StringFilter owner) {
        this.owner = owner;
    }

    public LongFilter getRifornimentoId() {
        return rifornimentoId;
    }

    public LongFilter rifornimentoId() {
        if (rifornimentoId == null) {
            rifornimentoId = new LongFilter();
        }
        return rifornimentoId;
    }

    public void setRifornimentoId(LongFilter rifornimentoId) {
        this.rifornimentoId = rifornimentoId;
    }

    public LongFilter getMarchioId() {
        return marchioId;
    }

    public LongFilter marchioId() {
        if (marchioId == null) {
            marchioId = new LongFilter();
        }
        return marchioId;
    }

    public void setMarchioId(LongFilter marchioId) {
        this.marchioId = marchioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GestoreCriteria that = (GestoreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provincia, that.provincia) &&
            Objects.equals(comune, that.comune) &&
            Objects.equals(indirizzo, that.indirizzo) &&
            Objects.equals(longitudine, that.longitudine) &&
            Objects.equals(latitudine, that.latitudine) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(benzinaPrezzoAlLitro, that.benzinaPrezzoAlLitro) &&
            Objects.equals(gasolioPrezzoAlLitro, that.gasolioPrezzoAlLitro) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(rifornimentoId, that.rifornimentoId) &&
            Objects.equals(marchioId, that.marchioId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            provincia,
            comune,
            indirizzo,
            longitudine,
            latitudine,
            tipo,
            benzinaPrezzoAlLitro,
            gasolioPrezzoAlLitro,
            owner,
            rifornimentoId,
            marchioId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestoreCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (provincia != null ? "provincia=" + provincia + ", " : "") +
            (comune != null ? "comune=" + comune + ", " : "") +
            (indirizzo != null ? "indirizzo=" + indirizzo + ", " : "") +
            (longitudine != null ? "longitudine=" + longitudine + ", " : "") +
            (latitudine != null ? "latitudine=" + latitudine + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (benzinaPrezzoAlLitro != null ? "benzinaPrezzoAlLitro=" + benzinaPrezzoAlLitro + ", " : "") +
            (gasolioPrezzoAlLitro != null ? "gasolioPrezzoAlLitro=" + gasolioPrezzoAlLitro + ", " : "") +
            (owner != null ? "owner=" + owner + ", " : "") +
            (rifornimentoId != null ? "rifornimentoId=" + rifornimentoId + ", " : "") +
            (marchioId != null ? "marchioId=" + marchioId + ", " : "") +
            "}";
    }
}
