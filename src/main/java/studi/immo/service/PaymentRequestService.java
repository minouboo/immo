package studi.immo.service;

import studi.immo.entity.PaymentRequest;

import java.util.List;

public interface PaymentRequestService {

    PaymentRequest savePaymentRequest (PaymentRequest paymentRequest);

    List<PaymentRequest> getPaymentRequestsByAgreementId (Long agreementId);

    PaymentRequest getPaymentRequestById (Long id);

    void deletePaymentRequest (Long id);

}
