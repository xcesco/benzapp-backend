package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Delega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Delega entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DelegaRepository extends JpaRepository<Delega, Long>, JpaSpecificationExecutor<Delega> {
    @Query("SELECT d FROM Delega d JOIN d.cittadino c WHERE c.owner= :owner")
    Page<Delega> findByCittadinoOwner(@Param("owner") String owner, Pageable page);
}
