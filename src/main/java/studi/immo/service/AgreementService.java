package studi.immo.service;

import studi.immo.entity.Agreement;

import java.util.List;

public interface AgreementService {

    Agreement saveAgreement (Agreement agreement);
    Agreement getAgreementById (Long id);
    List<Agreement> getMyAgreementsByUserId (Long userId);

    void deleteAgreementById (Long id);
}
