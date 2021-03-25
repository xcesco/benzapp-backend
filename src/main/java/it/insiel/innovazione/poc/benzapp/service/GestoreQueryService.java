package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.*; // for static metamodels
import it.insiel.innovazione.poc.benzapp.domain.Gestore;
import it.insiel.innovazione.poc.benzapp.repository.GestoreRepository;
import it.insiel.innovazione.poc.benzapp.security.SecurityUtils;
import it.insiel.innovazione.poc.benzapp.service.dto.GestoreCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Gestore} entities in the database.
 * The main input is a {@link GestoreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Gestore} or a {@link Page} of {@link Gestore} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GestoreQueryService extends QueryService<Gestore> {

    private final Logger log = LoggerFactory.getLogger(GestoreQueryService.class);

    private final GestoreRepository gestoreRepository;

    public GestoreQueryService(GestoreRepository gestoreRepository) {
        this.gestoreRepository = gestoreRepository;
    }

    /**
     * Return a {@link List} of {@link Gestore} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Gestore> findByCriteria(GestoreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Gestore> specification = createSpecification(criteria);
        return gestoreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Gestore} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Gestore> findByCriteria(GestoreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Gestore> specification = createSpecification(criteria);

        if (SecurityUtils.hasCurrentUserRole(SecurityUtils.Roles.ROLE_PATROL_STATION)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return gestoreRepository.findByOwner(authentication.getName(), page);
        }
        return gestoreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GestoreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Gestore> specification = createSpecification(criteria);
        return gestoreRepository.count(specification);
    }

    /**
     * Function to convert {@link GestoreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Gestore> createSpecification(GestoreCriteria criteria) {
        Specification<Gestore> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Gestore_.id));
            }
            if (criteria.getProvincia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProvincia(), Gestore_.provincia));
            }
            if (criteria.getComune() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComune(), Gestore_.comune));
            }
            if (criteria.getIndirizzo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIndirizzo(), Gestore_.indirizzo));
            }
            if (criteria.getLongitudine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitudine(), Gestore_.longitudine));
            }
            if (criteria.getLatitudine() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitudine(), Gestore_.latitudine));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), Gestore_.tipo));
            }
            if (criteria.getBenzinaPrezzoAlLitro() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getBenzinaPrezzoAlLitro(), Gestore_.benzinaPrezzoAlLitro));
            }
            if (criteria.getGasolioPrezzoAlLitro() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getGasolioPrezzoAlLitro(), Gestore_.gasolioPrezzoAlLitro));
            }
            if (criteria.getOwner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOwner(), Gestore_.owner));
            }
            if (criteria.getRifornimentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRifornimentoId(),
                            root -> root.join(Gestore_.rifornimentos, JoinType.LEFT).get(Rifornimento_.id)
                        )
                    );
            }
            if (criteria.getMarchioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMarchioId(), root -> root.join(Gestore_.marchio, JoinType.LEFT).get(Marchio_.id))
                    );
            }
        }
        return specification;
    }
}
