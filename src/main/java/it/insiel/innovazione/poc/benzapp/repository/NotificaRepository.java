package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Notifica;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Notifica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificaRepository extends JpaRepository<Notifica, Long>, JpaSpecificationExecutor<Notifica> {
    Optional<Notifica> findByTarga(String targa);
}
