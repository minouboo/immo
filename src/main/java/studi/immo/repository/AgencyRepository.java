package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Agency;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
}
