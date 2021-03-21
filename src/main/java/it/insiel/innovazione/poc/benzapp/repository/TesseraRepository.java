package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tessera entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TesseraRepository extends JpaRepository<Tessera, Long>, JpaSpecificationExecutor<Tessera> {}
