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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link it.insiel.innovazione.poc.benzapp.domain.Notifica} entity. This class is used
 * in {@link it.insiel.innovazione.poc.benzapp.web.rest.NotificaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notificas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotificaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter targa;

    private ZonedDateTimeFilter data;

    public NotificaCriteria() {}

    public NotificaCriteria(NotificaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.targa = other.targa == null ? null : other.targa.copy();
        this.data = other.data == null ? null : other.data.copy();
    }

    @Override
    public NotificaCriteria copy() {
        return new NotificaCriteria(this);
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

    public StringFilter getTarga() {
        return targa;
    }

    public StringFilter targa() {
        if (targa == null) {
            targa = new StringFilter();
        }
        return targa;
    }

    public void setTarga(StringFilter targa) {
        this.targa = targa;
    }

    public ZonedDateTimeFilter getData() {
        return data;
    }

    public ZonedDateTimeFilter data() {
        if (data == null) {
            data = new ZonedDateTimeFilter();
        }
        return data;
    }

    public void setData(ZonedDateTimeFilter data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificaCriteria that = (NotificaCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(targa, that.targa) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, targa, data);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (targa != null ? "targa=" + targa + ", " : "") +
            (data != null ? "data=" + data + ", " : "") +
            "}";
    }
}
