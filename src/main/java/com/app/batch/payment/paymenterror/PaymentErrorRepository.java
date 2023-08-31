package com.app.batch.payment.paymenterror;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-22
 * Time :
 * Remark :
 */
@Repository
public interface PaymentErrorRepository extends JpaRepository<PaymentError, Long>, JpaSpecificationExecutor<PaymentError> {

    Optional<PaymentError> findPaymentErrorByPayIdAndPeState(Long payId, String peState);

}