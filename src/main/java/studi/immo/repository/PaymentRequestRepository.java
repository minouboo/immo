package studi.immo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studi.immo.entity.PaymentRequest;

@Repository
public interface PaymentRequestRepository extends JpaRepository <PaymentRequest, Long> {
}
