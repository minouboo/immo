package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Tenant;
import studi.immo.repository.TenantRepository;
import studi.immo.service.TenantService;

@Service
public class TenantServiceImplement implements TenantService {

    private TenantRepository tenantRepository;

    public TenantServiceImplement (TenantRepository tenantRepository){
        super();
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant getTenantByUserId(Long userId) {
        return tenantRepository.getTenantByUserId(userId);
    }

    @Override
    public void deleteTenantById(Long id) {
        tenantRepository.deleteById(id);
    }
}
