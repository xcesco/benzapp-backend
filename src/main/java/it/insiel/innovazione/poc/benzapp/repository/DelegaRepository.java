package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Delega;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Delega entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DelegaRepository extends JpaRepository<Delega, Long>, JpaSpecificationExecutor<Delega> {}
