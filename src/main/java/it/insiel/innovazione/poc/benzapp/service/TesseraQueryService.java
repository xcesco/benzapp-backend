package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.*; // for static metamodels
import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import it.insiel.innovazione.poc.benzapp.repository.TesseraRepository;
import it.insiel.innovazione.poc.benzapp.service.dto.TesseraCriteria;
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
 * Service for executing complex queries for {@link Tessera} entities in the database.
 * The main input is a {@link TesseraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Tessera} or a {@link Page} of {@link Tessera} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TesseraQueryService extends QueryService<Tessera> {

    private final Logger log = LoggerFactory.getLogger(TesseraQueryService.class);

    private final TesseraRepository tesseraRepository;

    public TesseraQueryService(TesseraRepository tesseraRepository) {
        this.tesseraRepository = tesseraRepository;
    }

    /**
     * Return a {@link List} of {@link Tessera} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Tessera> findByCriteria(TesseraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tessera> specification = createSpecification(criteria);
        return tesseraRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Tessera} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Tessera> findByCriteria(TesseraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tessera> specification = createSpecification(criteria);
        return tesseraRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TesseraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tessera> specification = createSpecification(criteria);
        return tesseraRepository.count(specification);
    }

    /**
     * Function to convert {@link TesseraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tessera> createSpecification(TesseraCriteria criteria) {
        Specification<Tessera> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Tessera_.id));
            }
            if (criteria.getCodice() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodice(), Tessera_.codice));
            }
            if (criteria.getDataEmissione() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataEmissione(), Tessera_.dataEmissione));
            }
            if (criteria.getTarga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTarga(), Tessera_.targa));
            }
            if (criteria.getVeicolo() != null) {
                specification = specification.and(buildSpecification(criteria.getVeicolo(), Tessera_.veicolo));
            }
            if (criteria.getCarburante() != null) {
                specification = specification.and(buildSpecification(criteria.getCarburante(), Tessera_.carburante));
            }
            if (criteria.getDelegaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDelegaId(), root -> root.join(Tessera_.delegas, JoinType.LEFT).get(Delega_.id))
                    );
            }
            if (criteria.getRifornimentoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRifornimentoId(),
                            root -> root.join(Tessera_.rifornimentos, JoinType.LEFT).get(Rifornimento_.id)
                        )
                    );
            }
            if (criteria.getCittadinoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCittadinoId(),
                            root -> root.join(Tessera_.cittadino, JoinType.LEFT).get(Cittadino_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
