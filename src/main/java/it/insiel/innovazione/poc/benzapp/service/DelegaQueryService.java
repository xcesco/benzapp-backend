package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.*; // for static metamodels
import it.insiel.innovazione.poc.benzapp.domain.Delega;
import it.insiel.innovazione.poc.benzapp.repository.DelegaRepository;
import it.insiel.innovazione.poc.benzapp.security.SecurityUtils;
import it.insiel.innovazione.poc.benzapp.service.dto.DelegaCriteria;
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
 * Service for executing complex queries for {@link Delega} entities in the database.
 * The main input is a {@link DelegaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Delega} or a {@link Page} of {@link Delega} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DelegaQueryService extends QueryService<Delega> {

    private final Logger log = LoggerFactory.getLogger(DelegaQueryService.class);

    private final DelegaRepository delegaRepository;

    public DelegaQueryService(DelegaRepository delegaRepository) {
        this.delegaRepository = delegaRepository;
    }

    /**
     * Return a {@link List} of {@link Delega} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Delega> findByCriteria(DelegaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Delega> specification = createSpecification(criteria);
        return delegaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Delega} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Delega> findByCriteria(DelegaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Delega> specification = createSpecification(criteria);

        if (SecurityUtils.hasCurrentUserRole(SecurityUtils.Roles.ROLE_USER)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return delegaRepository.findByCittadinoOwner(authentication.getName(), page);
        }

        return delegaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DelegaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Delega> specification = createSpecification(criteria);
        return delegaRepository.count(specification);
    }

    /**
     * Function to convert {@link DelegaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Delega> createSpecification(DelegaCriteria criteria) {
        Specification<Delega> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Delega_.id));
            }
            if (criteria.getCittadinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCittadinoId(),
                            root -> root.join(Delega_.cittadino, JoinType.LEFT).get(Cittadino_.id)
                        )
                    );
            }
            if (criteria.getTesseraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTesseraId(), root -> root.join(Delega_.tessera, JoinType.LEFT).get(Tessera_.id))
                    );
            }
        }
        return specification;
    }
}
