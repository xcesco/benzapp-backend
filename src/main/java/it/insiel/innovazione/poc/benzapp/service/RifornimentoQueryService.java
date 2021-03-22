package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.*; // for static metamodels
import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.repository.RifornimentoRepository;
import it.insiel.innovazione.poc.benzapp.service.dto.RifornimentoCriteria;
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
 * Service for executing complex queries for {@link Rifornimento} entities in the database.
 * The main input is a {@link RifornimentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Rifornimento} or a {@link Page} of {@link Rifornimento} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RifornimentoQueryService extends QueryService<Rifornimento> {

    private final Logger log = LoggerFactory.getLogger(RifornimentoQueryService.class);

    private final RifornimentoRepository rifornimentoRepository;

    public RifornimentoQueryService(RifornimentoRepository rifornimentoRepository) {
        this.rifornimentoRepository = rifornimentoRepository;
    }

    /**
     * Return a {@link List} of {@link Rifornimento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Rifornimento> findByCriteria(RifornimentoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Rifornimento> specification = createSpecification(criteria);
        return rifornimentoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Rifornimento} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Rifornimento> findByCriteria(RifornimentoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rifornimento> specification = createSpecification(criteria);
        return rifornimentoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RifornimentoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Rifornimento> specification = createSpecification(criteria);
        return rifornimentoRepository.count(specification);
    }

    /**
     * Function to convert {@link RifornimentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rifornimento> createSpecification(RifornimentoCriteria criteria) {
        Specification<Rifornimento> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Rifornimento_.id));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Rifornimento_.data));
            }
            if (criteria.getLitriErogati() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLitriErogati(), Rifornimento_.litriErogati));
            }
            if (criteria.getSconto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSconto(), Rifornimento_.sconto));
            }
            if (criteria.getPrezzoAlLitro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrezzoAlLitro(), Rifornimento_.prezzoAlLitro));
            }
            if (criteria.getTipoCarburante() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoCarburante(), Rifornimento_.tipoCarburante));
            }
            if (criteria.getGestoreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGestoreId(),
                            root -> root.join(Rifornimento_.gestore, JoinType.LEFT).get(Gestore_.id)
                        )
                    );
            }
            if (criteria.getTesseraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTesseraId(),
                            root -> root.join(Rifornimento_.tessera, JoinType.LEFT).get(Tessera_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
