package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Accommodation;

@Repository
public interface AccomodationRepository extends JpaRepository <Accommodation, Long> {
}
