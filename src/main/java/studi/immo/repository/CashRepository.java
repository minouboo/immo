package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Cash;

@Repository
public interface CashRepository extends JpaRepository <Cash, Long> {
}
