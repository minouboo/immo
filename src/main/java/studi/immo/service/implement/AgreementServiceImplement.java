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

    @Override
    public List<Agreement> getMyAgreementsValidatedByUserId(Long userId) {
        return agreementRepository.getMyAgreementsValidatedByUserId(userId);
    }

    @Override
    public void deleteAgreementById(Long id) {
        agreementRepository.deleteById(id);
    }

    @Override
    public List<Agreement> getAllAgreementByAccommodationById(Long accommodationId) {
        return agreementRepository.getAllAgreementByAccommodationById(accommodationId);
    }

    @Override
    public List<Agreement> getAllAgreementValidatedByAccommodationById(Long accommodationId) {
        return agreementRepository.getAllAgreementValidatedByAccommodationById(accommodationId);
    }

    @Override
    public List<Agreement> getAllAgreementTerminatedByAccommodationById(Long accommodationId) {
        return agreementRepository.getAllAgreementTerminatedByAccommodationById(accommodationId);
    }

    @Override
    public List<Agreement> getAllAgreementWithAccommodationId(Long accommodationId) {
        return agreementRepository.getAllAgreementWithAccommodationId(accommodationId);
    }

    @Override
    public List<Agreement> getAllAgreementsTerminatedByUserId(Long userId) {
        return agreementRepository.getAllAgreementsTerminatedByUserId(userId);
    }


}
