package com.app.batch.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-07-05
 * Time :
 * Remark : EmailRepositoryCustom 쿼리문 선언부
 */
@Slf4j
@Repository
public class EmailRepositoryCustomImpl extends QuerydslRepositorySupport implements EmailRepositoryCustom {

    public EmailRepositoryCustomImpl() {
        super(Email.class);
    }

    @Override
    public Integer findByMonthSendPrice(String cpCode, String emYyyymm) {

        QEmail email = QEmail.email;

        Integer query = from(email)
                .select(email.emSendAllCount.sum())
                .where(email.cpCode.eq(cpCode), email.emYyyymm.eq(emYyyymm))
                .fetchOne();

        if (query == null) {
            query = 0;
        }

        return query;
    }

}
