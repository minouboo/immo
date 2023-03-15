package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.PaymentRequest;
import studi.immo.repository.PaymentRequestRepository;
import studi.immo.service.PaymentRequestService;

import java.util.List;

@Service
public class PaymentRequestServiceImplement implements PaymentRequestService {

    private PaymentRequestRepository paymentRequestRepository;

    public PaymentRequestServiceImplement(PaymentRequestRepository paymentRequestRepository) {
        this.paymentRequestRepository = paymentRequestRepository;
    }


    @Override
    public PaymentRequest savePaymentRequest(PaymentRequest paymentRequest) {
        return paymentRequestRepository.save(paymentRequest);
    }

    @Override
    public List<PaymentRequest> getPaymentRequestsByAgreementId(Long agreementId) {
        return paymentRequestRepository.getPaymentRequestsByAgreementId(agreementId);
    }

    @Override
    public PaymentRequest getPaymentRequestById(Long id) {
        return paymentRequestRepository.findById(id).get();
    }

    @Override
    public void deletePaymentRequest(Long id) {
        paymentRequestRepository.deleteById(id);
    }

}
