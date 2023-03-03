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

    @Query(value = "select * from agreement_user au join agreement a on a.id = au.agreement_id  where user_id = :userid", nativeQuery = true)
    List<Agreement> getMyAgreementsByUserId (@Param("userid")Long userId);

}
