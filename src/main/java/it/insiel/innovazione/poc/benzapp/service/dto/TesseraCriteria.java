package it.insiel.innovazione.poc.benzapp.service.dto;

import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoVeicolo;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link it.insiel.innovazione.poc.benzapp.domain.Tessera} entity. This class is used
 * in {@link it.insiel.innovazione.poc.benzapp.web.rest.TesseraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tesseras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TesseraCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoVeicolo
     */
    public static class TipoVeicoloFilter extends Filter<TipoVeicolo> {

        public TipoVeicoloFilter() {}

        public TipoVeicoloFilter(TipoVeicoloFilter filter) {
            super(filter);
        }

        @Override
        public TipoVeicoloFilter copy() {
            return new TipoVeicoloFilter(this);
        }
    }

    /**
     * Class for filtering TipoCarburante
     */
    public static class TipoCarburanteFilter extends Filter<TipoCarburante> {

        public TipoCarburanteFilter() {}

        public TipoCarburanteFilter(TipoCarburanteFilter filter) {
            super(filter);
        }

        @Override
        public TipoCarburanteFilter copy() {
            return new TipoCarburanteFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codice;

    private ZonedDateTimeFilter dataEmissione;

    private StringFilter targa;

    private TipoVeicoloFilter veicolo;

    private TipoCarburanteFilter carburante;

    private LongFilter rifornimentoId;

    private LongFilter delegaId;

    private LongFilter cittadinoId;

    public TesseraCriteria() {}

    public TesseraCriteria(TesseraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codice = other.codice == null ? null : other.codice.copy();
        this.dataEmissione = other.dataEmissione == null ? null : other.dataEmissione.copy();
        this.targa = other.targa == null ? null : other.targa.copy();
        this.veicolo = other.veicolo == null ? null : other.veicolo.copy();
        this.carburante = other.carburante == null ? null : other.carburante.copy();
        this.rifornimentoId = other.rifornimentoId == null ? null : other.rifornimentoId.copy();
        this.delegaId = other.delegaId == null ? null : other.delegaId.copy();
        this.cittadinoId = other.cittadinoId == null ? null : other.cittadinoId.copy();
    }

    @Override
    public TesseraCriteria copy() {
        return new TesseraCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCodice() {
        return codice;
    }

    public void setCodice(StringFilter codice) {
        this.codice = codice;
    }

    public ZonedDateTimeFilter getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(ZonedDateTimeFilter dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public StringFilter getTarga() {
        return targa;
    }

    public void setTarga(StringFilter targa) {
        this.targa = targa;
    }

    public TipoVeicoloFilter getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(TipoVeicoloFilter veicolo) {
        this.veicolo = veicolo;
    }

    public TipoCarburanteFilter getCarburante() {
        return carburante;
    }

    public void setCarburante(TipoCarburanteFilter carburante) {
        this.carburante = carburante;
    }

    public LongFilter getRifornimentoId() {
        return rifornimentoId;
    }

    public void setRifornimentoId(LongFilter rifornimentoId) {
        this.rifornimentoId = rifornimentoId;
    }

    public LongFilter getDelegaId() {
        return delegaId;
    }

    public void setDelegaId(LongFilter delegaId) {
        this.delegaId = delegaId;
    }

    public LongFilter getCittadinoId() {
        return cittadinoId;
    }

    public void setCittadinoId(LongFilter cittadinoId) {
        this.cittadinoId = cittadinoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TesseraCriteria that = (TesseraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codice, that.codice) &&
            Objects.equals(dataEmissione, that.dataEmissione) &&
            Objects.equals(targa, that.targa) &&
            Objects.equals(veicolo, that.veicolo) &&
            Objects.equals(carburante, that.carburante) &&
            Objects.equals(rifornimentoId, that.rifornimentoId) &&
            Objects.equals(delegaId, that.delegaId) &&
            Objects.equals(cittadinoId, that.cittadinoId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codice, dataEmissione, targa, veicolo, carburante, rifornimentoId, delegaId, cittadinoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TesseraCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (codice != null ? "codice=" + codice + ", " : "") +
                (dataEmissione != null ? "dataEmissione=" + dataEmissione + ", " : "") +
                (targa != null ? "targa=" + targa + ", " : "") +
                (veicolo != null ? "veicolo=" + veicolo + ", " : "") +
                (carburante != null ? "carburante=" + carburante + ", " : "") +
                (rifornimentoId != null ? "rifornimentoId=" + rifornimentoId + ", " : "") +
                (delegaId != null ? "delegaId=" + delegaId + ", " : "") +
                (cittadinoId != null ? "cittadinoId=" + cittadinoId + ", " : "") +
            "}";
    }
}
