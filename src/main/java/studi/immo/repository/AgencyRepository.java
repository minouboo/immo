package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Agency;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

    @Query (value = "select * from agency a where user_id = :userid" ,nativeQuery = true)
    Agency getAgencyByUserId (@Param("userid")Long userId);

}
