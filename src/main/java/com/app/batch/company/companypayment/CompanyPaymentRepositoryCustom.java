package com.app.batch.company.companypayment;

import com.app.batch.company.companypayment.dtos.CompanyPaymentListDto;
import com.app.batch.company.companypayment.dtos.CompanyPaymentReservationListDto;
import com.app.batch.company.companypayment.dtos.CompanyPaymentSearchDto;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-17
 * Time :
 * Remark : CompanyPayment Sql 쿼리호출
 */
public interface CompanyPaymentRepositoryCustom {

    List<CompanyPaymentListDto> findByPaymentList(LocalDate date);

    List<CompanyPaymentReservationListDto> findByPaymentReservationList(LocalDate date);

    CompanyPaymentSearchDto findByPaymentSearch(String cpCode);

}