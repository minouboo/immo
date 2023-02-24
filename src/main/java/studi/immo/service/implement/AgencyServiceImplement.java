package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Agency;
import studi.immo.repository.AgencyRepository;
import studi.immo.service.AgencyService;

@Service
public class AgencyServiceImplement implements AgencyService {

    private AgencyRepository agencyRepository;

    public AgencyServiceImplement (AgencyRepository agencyRepository){
        super();
        this.agencyRepository = agencyRepository;
    }


    @Override
    public Agency saveAgency(Agency agency) {
        return agencyRepository.save(agency);
    }

    @Override
    public Agency getAgencyByUserId(Long userId) {
        return agencyRepository.getAgencyByUserId(userId);
    }
}
