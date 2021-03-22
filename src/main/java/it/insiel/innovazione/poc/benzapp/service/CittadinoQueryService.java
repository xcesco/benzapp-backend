package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.*; // for static metamodels
import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import it.insiel.innovazione.poc.benzapp.repository.CittadinoRepository;
import it.insiel.innovazione.poc.benzapp.service.dto.CittadinoCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cittadino} entities in the database.
 * The main input is a {@link CittadinoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cittadino} or a {@link Page} of {@link Cittadino} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CittadinoQueryService extends QueryService<Cittadino> {

    private final Logger log = LoggerFactory.getLogger(CittadinoQueryService.class);

    private final CittadinoRepository cittadinoRepository;

    public CittadinoQueryService(CittadinoRepository cittadinoRepository) {
        this.cittadinoRepository = cittadinoRepository;
    }

    /**
     * Return a {@link List} of {@link Cittadino} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cittadino> findByCriteria(CittadinoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cittadino> specification = createSpecification(criteria);
        return cittadinoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cittadino} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cittadino> findByCriteria(CittadinoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cittadino> specification = createSpecification(criteria);
        return cittadinoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CittadinoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cittadino> specification = createSpecification(criteria);
        return cittadinoRepository.count(specification);
    }

    /**
     * Function to convert {@link CittadinoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cittadino> createSpecification(CittadinoCriteria criteria) {
        Specification<Cittadino> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cittadino_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Cittadino_.nome));
            }
            if (criteria.getCognome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCognome(), Cittadino_.cognome));
            }
            if (criteria.getCodiceFiscale() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodiceFiscale(), Cittadino_.codiceFiscale));
            }
            if (criteria.getDelegaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDelegaId(), root -> root.join(Cittadino_.delegas, JoinType.LEFT).get(Delega_.id))
                    );
            }
            if (criteria.getTesseraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTesseraId(), root -> root.join(Cittadino_.tesseras, JoinType.LEFT).get(Tessera_.id))
                    );
            }
            if (criteria.getFasciaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFasciaId(), root -> root.join(Cittadino_.fascia, JoinType.LEFT).get(Fascia_.id))
                    );
            }
        }
        return specification;
    }
}
