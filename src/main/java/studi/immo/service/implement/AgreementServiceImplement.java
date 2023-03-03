package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Agreement;
import studi.immo.repository.AgreementRepository;
import studi.immo.service.AgreementService;
import studi.immo.service.ChatRoomService;

import java.util.List;

@Service
public class AgreementServiceImplement implements AgreementService {

    private AgreementRepository agreementRepository;

    public AgreementServiceImplement (AgreementRepository agreementRepository){
        super ();
        this.agreementRepository = agreementRepository;
    }


    @Override
    public Agreement saveAgreement(Agreement agreement) {
        return agreementRepository.save(agreement);
    }

    @Override
    public Agreement getAgreementById(Long id) {
        return agreementRepository.findById(id).get();
    }

    @Override
    public List<Agreement> getMyAgreementsByUserId(Long userId) {
        return agreementRepository.getMyAgreementsByUserId(userId);
    }
}
