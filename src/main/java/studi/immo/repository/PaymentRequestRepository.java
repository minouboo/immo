package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studi.immo.entity.PaymentRequest;

import java.util.List;

@Repository
public interface PaymentRequestRepository extends JpaRepository <PaymentRequest, Long> {

    @Query(value = "select * from payment_request pr where agreement_id  = :agreementid order by issue_date desc",nativeQuery = true)
    List<PaymentRequest> getPaymentRequestsByAgreementId (@Param("agreementid")Long agreementId);

}
