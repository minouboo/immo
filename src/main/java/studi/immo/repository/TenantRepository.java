package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    @Query (value = "select * from tenant t where user_id = :userid",nativeQuery = true)
    Tenant getTenantByUserId (@Param("userid")Long userId);

}
