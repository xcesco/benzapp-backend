package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Cittadino;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cittadino entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CittadinoRepository extends JpaRepository<Cittadino, Long> {}
