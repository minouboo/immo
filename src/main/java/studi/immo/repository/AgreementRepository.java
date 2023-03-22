package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Agreement;
import studi.immo.entity.ApartmentInventory;

import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository <Agreement, Long> {

    @Query(value = "select * from agreement_user au join agreement a on a.id = au.agreement_id  where user_id = :userid and (tenant_validate = false or landlord_validate = false) ", nativeQuery = true)
    List<Agreement> getMyAgreementsByUserId (@Param("userid")Long userId);

    @Query (value = "select * from agreement_user au join agreement a on a.id = au.agreement_id  where tenant_validate = true and landlord_validate = true and  user_id = :userid", nativeQuery = true)
    List<Agreement> getMyAgreementsValidatedByUserId (@Param("userid")Long userId);

    @Query (value = "select * from agreement a where a.accommodation_id = :accommodationid and (tenant_validate = false or landlord_validate = false)", nativeQuery = true)
    List<Agreement> getAllAgreementByAccommodationById (@Param("accommodationid")Long accommodationId);

    @Query(value = "select * from agreement a where a.accommodation_id = :accommodationid and tenant_validate = true and landlord_validate = true ",nativeQuery = true)
    List<Agreement> getAllAgreementValidatedByAccommodationById (@Param("accommodationid")Long accommodationId);

}
