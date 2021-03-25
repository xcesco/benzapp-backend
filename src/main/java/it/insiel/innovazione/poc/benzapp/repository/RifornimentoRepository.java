package it.insiel.innovazione.poc.benzapp.repository;

import it.insiel.innovazione.poc.benzapp.domain.Rifornimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rifornimento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RifornimentoRepository extends JpaRepository<Rifornimento, Long>, JpaSpecificationExecutor<Rifornimento> {
    @Query("SELECT r FROM Rifornimento r JOIN r.tessera t JOIN t.cittadino c WHERE c.owner= :owner")
    Page<Rifornimento> findByCittadinoOwner(@Param("owner") String owner, Pageable pageable);

    @Query("SELECT r FROM Rifornimento r JOIN r.gestore g WHERE g.owner= :owner")
    Page<Rifornimento> findByGestoreOwner(@Param("owner") String owner, Pageable pageable);
}
