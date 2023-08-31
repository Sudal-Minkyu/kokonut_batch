package com.app.batch.payment.paymentprivacycount;

import com.app.batch.payment.paymentprivacycount.dtos.PaymentPrivacyCountMonthAverageDto;
import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : PaymentPrivacy Sql 쿼리호출
 */
public interface PaymentPrivacyCountRepositoryCustom {

    PaymentPrivacyCountMonthAverageDto findByMonthPrivacyCount(String cpCode, String ctName, LocalDate firstDate, LocalDate lastDate); // 월평균 개인정보 수 호출

}