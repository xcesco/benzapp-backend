package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import it.insiel.innovazione.poc.benzapp.domain.Tessera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tessera entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TesseraRepository extends JpaRepository<Tessera, Long>, JpaSpecificationExecutor<Tessera> {
    @Query("SELECT t FROM Tessera t JOIN t.cittadino c  WHERE c.owner= :owner")
    Page<Tessera> findByCittadinoOwner(@Param("owner") String owner, Pageable pageable);
}
