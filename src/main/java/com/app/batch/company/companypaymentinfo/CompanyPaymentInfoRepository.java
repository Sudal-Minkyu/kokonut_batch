package com.app.batch.company.companypaymentinfo;

import com.app.batch.company.companypayment.CompanyPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark :
 */
@Repository
public interface CompanyPaymentInfoRepository extends JpaRepository<CompanyPaymentInfo, Long>, JpaSpecificationExecutor<CompanyPayment> {

    Optional<CompanyPaymentInfo> findCompanyPaymentInfoByCpiId(Long cpiId);

}