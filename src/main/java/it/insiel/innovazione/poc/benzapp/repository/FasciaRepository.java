package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Fascia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fascia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FasciaRepository extends JpaRepository<Fascia, Long> {}
