package it.insiel.innovazione.poc.benzapp.service.dto;

import it.insiel.innovazione.poc.benzapp.domain.enumeration.TipoCarburante;
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
 * Criteria class for the {@link it.insiel.innovazione.poc.benzapp.domain.Rifornimento} entity. This class is used
 * in {@link it.insiel.innovazione.poc.benzapp.web.rest.RifornimentoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rifornimentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RifornimentoCriteria implements Serializable, Criteria {

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

    private ZonedDateTimeFilter data;

    private IntegerFilter progressivo;

    private FloatFilter litriErogati;

    private FloatFilter sconto;

    private FloatFilter prezzoAlLitro;

    private TipoCarburanteFilter tipoCarburante;

    private LongFilter gestoreId;

    private LongFilter tesseraId;

    public RifornimentoCriteria() {}

    public RifornimentoCriteria(RifornimentoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.data = other.data == null ? null : other.data.copy();
        this.progressivo = other.progressivo == null ? null : other.progressivo.copy();
        this.litriErogati = other.litriErogati == null ? null : other.litriErogati.copy();
        this.sconto = other.sconto == null ? null : other.sconto.copy();
        this.prezzoAlLitro = other.prezzoAlLitro == null ? null : other.prezzoAlLitro.copy();
        this.tipoCarburante = other.tipoCarburante == null ? null : other.tipoCarburante.copy();
        this.gestoreId = other.gestoreId == null ? null : other.gestoreId.copy();
        this.tesseraId = other.tesseraId == null ? null : other.tesseraId.copy();
    }

    @Override
    public RifornimentoCriteria copy() {
        return new RifornimentoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getData() {
        return data;
    }

    public void setData(ZonedDateTimeFilter data) {
        this.data = data;
    }

    public IntegerFilter getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(IntegerFilter progressivo) {
        this.progressivo = progressivo;
    }

    public FloatFilter getLitriErogati() {
        return litriErogati;
    }

    public void setLitriErogati(FloatFilter litriErogati) {
        this.litriErogati = litriErogati;
    }

    public FloatFilter getSconto() {
        return sconto;
    }

    public void setSconto(FloatFilter sconto) {
        this.sconto = sconto;
    }

    public FloatFilter getPrezzoAlLitro() {
        return prezzoAlLitro;
    }

    public void setPrezzoAlLitro(FloatFilter prezzoAlLitro) {
        this.prezzoAlLitro = prezzoAlLitro;
    }

    public TipoCarburanteFilter getTipoCarburante() {
        return tipoCarburante;
    }

    public void setTipoCarburante(TipoCarburanteFilter tipoCarburante) {
        this.tipoCarburante = tipoCarburante;
    }

    public LongFilter getGestoreId() {
        return gestoreId;
    }

    public void setGestoreId(LongFilter gestoreId) {
        this.gestoreId = gestoreId;
    }

    public LongFilter getTesseraId() {
        return tesseraId;
    }

    public void setTesseraId(LongFilter tesseraId) {
        this.tesseraId = tesseraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RifornimentoCriteria that = (RifornimentoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(data, that.data) &&
            Objects.equals(progressivo, that.progressivo) &&
            Objects.equals(litriErogati, that.litriErogati) &&
            Objects.equals(sconto, that.sconto) &&
            Objects.equals(prezzoAlLitro, that.prezzoAlLitro) &&
            Objects.equals(tipoCarburante, that.tipoCarburante) &&
            Objects.equals(gestoreId, that.gestoreId) &&
            Objects.equals(tesseraId, that.tesseraId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, progressivo, litriErogati, sconto, prezzoAlLitro, tipoCarburante, gestoreId, tesseraId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RifornimentoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (data != null ? "data=" + data + ", " : "") +
                (progressivo != null ? "progressivo=" + progressivo + ", " : "") +
                (litriErogati != null ? "litriErogati=" + litriErogati + ", " : "") +
                (sconto != null ? "sconto=" + sconto + ", " : "") +
                (prezzoAlLitro != null ? "prezzoAlLitro=" + prezzoAlLitro + ", " : "") +
                (tipoCarburante != null ? "tipoCarburante=" + tipoCarburante + ", " : "") +
                (gestoreId != null ? "gestoreId=" + gestoreId + ", " : "") +
                (tesseraId != null ? "tesseraId=" + tesseraId + ", " : "") +
            "}";
    }
}
