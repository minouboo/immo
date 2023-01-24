package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Agreement;

@Repository
public interface AgreementRepository extends JpaRepository <Agreement, Long> {
}
