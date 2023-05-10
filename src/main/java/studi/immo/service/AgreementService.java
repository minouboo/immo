package studi.immo.service;

import studi.immo.entity.Agreement;

import java.util.List;

public interface AgreementService {

    Agreement saveAgreement (Agreement agreement);
    Agreement getAgreementById (Long id);
    List<Agreement> getMyAgreementsByUserId (Long userId);
    List<Agreement> getMyAgreementsValidatedByUserId (Long userId);
    List<Agreement> getAllAgreementsTerminatedByUserId (Long userId);
    void deleteAgreementById (Long id);
    List<Agreement> getAllAgreementByAccommodationById (Long accommodationId);
    List<Agreement> getAllAgreementValidatedByAccommodationById (Long accommodationId);
    List<Agreement> getAllAgreementTerminatedByAccommodationById (Long accommodationId);
    List<Agreement> getAllAgreementWithAccommodationId (Long accommodationId);

}
