package it.insiel.innovazione.poc.benzapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Marchio.
 */
@Entity
@Table(name = "marchi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Marchio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "immagine")
    private byte[] immagine;

    @Column(name = "immagine_content_type")
    private String immagineContentType;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "marchio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "rifornimentos", "marchio" }, allowSetters = true)
    private Set<Gestore> gestores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Marchio id(Long id) {
        this.id = id;
        return this;
    }

    public byte[] getImmagine() {
        return this.immagine;
    }

    public Marchio immagine(byte[] immagine) {
        this.immagine = immagine;
        return this;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public String getImmagineContentType() {
        return this.immagineContentType;
    }

    public Marchio immagineContentType(String immagineContentType) {
        this.immagineContentType = immagineContentType;
        return this;
    }

    public void setImmagineContentType(String immagineContentType) {
        this.immagineContentType = immagineContentType;
    }

    public String getNome() {
        return this.nome;
    }

    public Marchio nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Gestore> getGestores() {
        return this.gestores;
    }

    public Marchio gestores(Set<Gestore> gestores) {
        this.setGestores(gestores);
        return this;
    }

    public Marchio addGestore(Gestore gestore) {
        this.gestores.add(gestore);
        gestore.setMarchio(this);
        return this;
    }

    public Marchio removeGestore(Gestore gestore) {
        this.gestores.remove(gestore);
        gestore.setMarchio(null);
        return this;
    }

    public void setGestores(Set<Gestore> gestores) {
        if (this.gestores != null) {
            this.gestores.forEach(i -> i.setMarchio(null));
        }
        if (gestores != null) {
            gestores.forEach(i -> i.setMarchio(this));
        }
        this.gestores = gestores;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marchio)) {
            return false;
        }
        return id != null && id.equals(((Marchio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marchio{" +
            "id=" + getId() +
            ", immagine='" + getImmagine() + "'" +
            ", immagineContentType='" + getImmagineContentType() + "'" +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
