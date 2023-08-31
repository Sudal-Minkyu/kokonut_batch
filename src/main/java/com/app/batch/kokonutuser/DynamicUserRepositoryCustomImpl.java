package com.app.batch.kokonutuser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @author Woody
 * Date : 2022-12-27
 * Time :
 * Remark : Kokonut-user DB & 테이블 - 쿼리문 선언부 모두 NativeQuery로 실행한다.
 */
@Slf4j
@Repository
public class DynamicUserRepositoryCustomImpl implements DynamicUserRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DynamicUserRepositoryCustomImpl(@Qualifier("kokonutUserJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 개인정보 리스트의 총합계
    public int privacyListTotal(String queryStr) {
        log.info("개인정보 리스트 카운팅 호출한 쿼리문 : "+queryStr);
        Integer result = jdbcTemplate.queryForObject(queryStr, Integer.class);
        return Objects.requireNonNullElse(result, 0);
    }


}
