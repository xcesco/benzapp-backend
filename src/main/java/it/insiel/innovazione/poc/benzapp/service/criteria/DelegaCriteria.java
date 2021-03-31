package it.insiel.innovazione.poc.benzapp.service.criteria;

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
 * Criteria class for the {@link it.insiel.innovazione.poc.benzapp.domain.Delega} entity. This class is used
 * in {@link it.insiel.innovazione.poc.benzapp.web.rest.DelegaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /delegas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DelegaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter cittadinoId;

    private LongFilter tesseraId;

    public DelegaCriteria() {}

    public DelegaCriteria(DelegaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cittadinoId = other.cittadinoId == null ? null : other.cittadinoId.copy();
        this.tesseraId = other.tesseraId == null ? null : other.tesseraId.copy();
    }

    @Override
    public DelegaCriteria copy() {
        return new DelegaCriteria(this);
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

    public LongFilter getCittadinoId() {
        return cittadinoId;
    }

    public LongFilter cittadinoId() {
        if (cittadinoId == null) {
            cittadinoId = new LongFilter();
        }
        return cittadinoId;
    }

    public void setCittadinoId(LongFilter cittadinoId) {
        this.cittadinoId = cittadinoId;
    }

    public LongFilter getTesseraId() {
        return tesseraId;
    }

    public LongFilter tesseraId() {
        if (tesseraId == null) {
            tesseraId = new LongFilter();
        }
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
        final DelegaCriteria that = (DelegaCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(cittadinoId, that.cittadinoId) && Objects.equals(tesseraId, that.tesseraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cittadinoId, tesseraId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DelegaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cittadinoId != null ? "cittadinoId=" + cittadinoId + ", " : "") +
            (tesseraId != null ? "tesseraId=" + tesseraId + ", " : "") +
            "}";
    }
}
