package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rifornimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RifornimentoRepository extends JpaRepository<Rifornimento, Long>, JpaSpecificationExecutor<Rifornimento> {}
