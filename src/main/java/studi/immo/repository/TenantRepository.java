package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
