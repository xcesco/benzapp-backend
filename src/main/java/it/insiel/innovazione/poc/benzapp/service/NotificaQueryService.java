package it.insiel.innovazione.poc.benzapp.service;

import it.insiel.innovazione.poc.benzapp.domain.*; // for static metamodels
import it.insiel.innovazione.poc.benzapp.domain.Notifica;
import it.insiel.innovazione.poc.benzapp.repository.NotificaRepository;
import it.insiel.innovazione.poc.benzapp.service.criteria.NotificaCriteria;
import it.insiel.innovazione.poc.benzapp.service.dto.NotificaDTO;
import it.insiel.innovazione.poc.benzapp.service.mapper.NotificaMapper;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Service for executing complex queries for {@link Notifica} entities in the database.
 * The main input is a {@link NotificaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotificaDTO} or a {@link Page} of {@link NotificaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificaQueryService extends QueryService<Notifica> {

    @Value("${app.notifica-interval}")
    private long interval;

    private final Logger log = LoggerFactory.getLogger(NotificaQueryService.class);

    private final NotificaRepository notificaRepository;

    private final NotificaMapper notificaMapper;

    public NotificaQueryService(NotificaRepository notificaRepository, NotificaMapper notificaMapper) {
        this.notificaRepository = notificaRepository;
        this.notificaMapper = notificaMapper;
    }

    /**
     * Return a {@link List} of {@link NotificaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotificaDTO> findByCriteria(NotificaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notifica> specification = createSpecification(criteria);
        return notificaMapper.toDto(notificaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotificaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificaDTO> findByCriteria(NotificaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notifica> specification = createSpecification(criteria);
        return notificaRepository.findAll(specification, page).map(notificaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Notifica> specification = createSpecification(criteria);
        return notificaRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notifica> createSpecification(NotificaCriteria criteria) {
        Specification<Notifica> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Notifica_.id));
            }
            if (criteria.getTarga() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTarga(), Notifica_.targa));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Notifica_.data));
            }
        }

        ZonedDateTime warningDate = ZonedDateTime.now().minus(Duration.ofSeconds(interval));
        log.debug(String.format("Request to get all Notificas %s", warningDate));

        ZonedDateTimeFilter ap = new ZonedDateTimeFilter().setGreaterThan(warningDate);
        specification = specification.and(buildRangeSpecification(ap, Notifica_.data));

        return specification;
    }
}
