package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository <Advertisement, Long> {
}
