package studi.immo.service;

import studi.immo.entity.Tenant;

public interface TenantService {

    Tenant saveTenant (Tenant tenant);

    Tenant getTenantByUserId (Long userId);

    void deleteTenantById (Long id);

}
