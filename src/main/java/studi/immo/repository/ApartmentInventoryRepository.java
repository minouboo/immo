package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.ApartmentInventory;

@Repository
public interface ApartmentInventoryRepository extends JpaRepository <ApartmentInventory, Long> {
}
