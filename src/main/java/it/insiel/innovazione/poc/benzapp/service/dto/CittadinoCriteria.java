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

    private StringFilter owner;

    private LongFilter delegaId;

    private LongFilter tesseraId;

    private LongFilter fasciaId;

    public CittadinoCriteria() {}

    public CittadinoCriteria(CittadinoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cognome = other.cognome == null ? null : other.cognome.copy();
        this.codiceFiscale = other.codiceFiscale == null ? null : other.codiceFiscale.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.delegaId = other.delegaId == null ? null : other.delegaId.copy();
        this.tesseraId = other.tesseraId == null ? null : other.tesseraId.copy();
        this.fasciaId = other.fasciaId == null ? null : other.fasciaId.copy();
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

    public StringFilter getOwner() {
        return owner;
    }

    public void setOwner(StringFilter owner) {
        this.owner = owner;
    }

    public LongFilter getDelegaId() {
        return delegaId;
    }

    public void setDelegaId(LongFilter delegaId) {
        this.delegaId = delegaId;
    }

    public LongFilter getTesseraId() {
        return tesseraId;
    }

    public void setTesseraId(LongFilter tesseraId) {
        this.tesseraId = tesseraId;
    }

    public LongFilter getFasciaId() {
        return fasciaId;
    }

    public void setFasciaId(LongFilter fasciaId) {
        this.fasciaId = fasciaId;
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
            Objects.equals(owner, that.owner) &&
            Objects.equals(delegaId, that.delegaId) &&
            Objects.equals(tesseraId, that.tesseraId) &&
            Objects.equals(fasciaId, that.fasciaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cognome, codiceFiscale, owner, delegaId, tesseraId, fasciaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CittadinoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (cognome != null ? "cognome=" + cognome + ", " : "") +
                (codiceFiscale != null ? "codiceFiscale=" + codiceFiscale + ", " : "") +
                (owner != null ? "owner=" + owner + ", " : "") +
                (delegaId != null ? "delegaId=" + delegaId + ", " : "") +
                (tesseraId != null ? "tesseraId=" + tesseraId + ", " : "") +
                (fasciaId != null ? "fasciaId=" + fasciaId + ", " : "") +
            "}";
    }
}
