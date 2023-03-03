package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.ApartmentInventory;

@Repository
public interface ApartmentInventoryRepository extends JpaRepository <ApartmentInventory, Long> {

    @Query (value = "select * from apartment_inventory ai where agreement_id = :agreementid",nativeQuery = true)
    ApartmentInventory getApartmentInventoryByAgreementId (@Param("agreementid")Long agreementId);

}
