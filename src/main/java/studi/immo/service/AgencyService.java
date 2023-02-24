package studi.immo.service;

import studi.immo.entity.Agency;

public interface AgencyService {

    Agency saveAgency (Agency agency );

    Agency getAgencyByUserId (Long userId);

}
