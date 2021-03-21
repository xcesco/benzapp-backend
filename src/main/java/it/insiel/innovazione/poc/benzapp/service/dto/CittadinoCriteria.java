package it.insiel.innovazione.poc.benzapp.service.dto;

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
 * Criteria class for the {@link it.insiel.innovazione.poc.benzapp.domain.Cittadino} entity. This class is used
 * in {@link it.insiel.innovazione.poc.benzapp.web.rest.CittadinoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cittadinos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CittadinoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cognome;

    private StringFilter codiceFiscale;

    private LongFilter tesseraId;

    private LongFilter delegaId;

    public CittadinoCriteria() {}

    public CittadinoCriteria(CittadinoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cognome = other.cognome == null ? null : other.cognome.copy();
        this.codiceFiscale = other.codiceFiscale == null ? null : other.codiceFiscale.copy();
        this.tesseraId = other.tesseraId == null ? null : other.tesseraId.copy();
        this.delegaId = other.delegaId == null ? null : other.delegaId.copy();
    }

    @Override
    public CittadinoCriteria copy() {
        return new CittadinoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCognome() {
        return cognome;
    }

    public void setCognome(StringFilter cognome) {
        this.cognome = cognome;
    }

    public StringFilter getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(StringFilter codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public LongFilter getTesseraId() {
        return tesseraId;
    }

    public void setTesseraId(LongFilter tesseraId) {
        this.tesseraId = tesseraId;
    }

    public LongFilter getDelegaId() {
        return delegaId;
    }

    public void setDelegaId(LongFilter delegaId) {
        this.delegaId = delegaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CittadinoCriteria that = (CittadinoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cognome, that.cognome) &&
            Objects.equals(codiceFiscale, that.codiceFiscale) &&
            Objects.equals(tesseraId, that.tesseraId) &&
            Objects.equals(delegaId, that.delegaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cognome, codiceFiscale, tesseraId, delegaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CittadinoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (cognome != null ? "cognome=" + cognome + ", " : "") +
                (codiceFiscale != null ? "codiceFiscale=" + codiceFiscale + ", " : "") +
                (tesseraId != null ? "tesseraId=" + tesseraId + ", " : "") +
                (delegaId != null ? "delegaId=" + delegaId + ", " : "") +
            "}";
    }
}
