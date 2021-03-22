package it.insiel.innovazione.poc.benzapp.service.dto;

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

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getProvincia() {
        return provincia;
    }

    public void setProvincia(StringFilter provincia) {
        this.provincia = provincia;
    }

    public StringFilter getComune() {
        return comune;
    }

    public void setComune(StringFilter comune) {
        this.comune = comune;
    }

    public StringFilter getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(StringFilter indirizzo) {
        this.indirizzo = indirizzo;
    }

    public FloatFilter getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(FloatFilter longitudine) {
        this.longitudine = longitudine;
    }

    public FloatFilter getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(FloatFilter latitudine) {
        this.latitudine = latitudine;
    }

    public TipoImpiantoFilter getTipo() {
        return tipo;
    }

    public void setTipo(TipoImpiantoFilter tipo) {
        this.tipo = tipo;
    }

    public FloatFilter getBenzinaPrezzoAlLitro() {
        return benzinaPrezzoAlLitro;
    }

    public void setBenzinaPrezzoAlLitro(FloatFilter benzinaPrezzoAlLitro) {
        this.benzinaPrezzoAlLitro = benzinaPrezzoAlLitro;
    }

    public FloatFilter getGasolioPrezzoAlLitro() {
        return gasolioPrezzoAlLitro;
    }

    public void setGasolioPrezzoAlLitro(FloatFilter gasolioPrezzoAlLitro) {
        this.gasolioPrezzoAlLitro = gasolioPrezzoAlLitro;
    }

    public LongFilter getRifornimentoId() {
        return rifornimentoId;
    }

    public void setRifornimentoId(LongFilter rifornimentoId) {
        this.rifornimentoId = rifornimentoId;
    }

    public LongFilter getMarchioId() {
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
                (rifornimentoId != null ? "rifornimentoId=" + rifornimentoId + ", " : "") +
                (marchioId != null ? "marchioId=" + marchioId + ", " : "") +
            "}";
    }
}
