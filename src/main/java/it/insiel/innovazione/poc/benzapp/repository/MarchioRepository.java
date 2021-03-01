package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Marchio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Marchio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarchioRepository extends JpaRepository<Marchio, Long> {}
