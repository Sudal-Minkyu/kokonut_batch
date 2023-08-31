package com.app.batch.email;

/**
 * @author Woody
 * Date : 2023-07-05
 * Time :
 * Remark : Email Sql 쿼리호출
 */
public interface EmailRepositoryCustom {

    Integer findByMonthSendPrice(String cpCode, String emYyyymm); // 월 발송횟수가져오기 -> 발송금액계산용

}