package com.app.batch.email;

import com.app.batch.email.dtos.EmailCheckDto;
import com.app.batch.utils.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-07
 * Remark :
 */
@Slf4j
@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    @Autowired
    public EmailService(EmailRepository emailRepository, MailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }

    // 매일 5분마다 실행
    // 이메일발송건 업데이트 처리
    @Transactional
    public void kokonutSendEmailUpdate(LocalDate localDate) {
        log.info("kokonutSendEmailUpdate 호출");

        log.info("현재날짜 : "+localDate);

        List<Email> emailList = emailRepository.findEmails(Arrays.asList("1", "2"), LocalDateTime.now()); // 미발송 or 발송준비중이면서 현재시간보다 낮은 상태인 메일만 조회
//        log.info("emailList : "+emailList);
//        log.info("emailList.size() : "+emailList.size());

        List<Email> updateEmailList = new ArrayList<>();

        for(Email email : emailList) {

            String requestId = email.getEmRequestId();
            if(requestId != null && !requestId.equals("")) {
//                log.info("requestId : "+requestId);

                EmailCheckDto emailCheckDto = mailSender.sendEmailCheck(requestId);
//                log.info("emailCheckDto : "+emailCheckDto);

                if(emailCheckDto != null) {
                    if(emailCheckDto.getFailCount() == emailCheckDto.getRequestCount()) {
                        // 발송실패
                        email.setEmState("4");
                    } else if(emailCheckDto.getFailCount() > 0){
                        // 일부실패
                        email.setEmState("3");
                    } else {
                        // 발송성공
                        email.setEmState("5");
                    }
                    email.setEmSendAllCount(emailCheckDto.getRequestCount());
                    email.setEmSendSucCount(emailCheckDto.getSentCount());
                    email.setEmSendFailCount(emailCheckDto.getFailCount());
                    updateEmailList.add(email);
                }
            }
        }

        emailRepository.saveAll(updateEmailList);
    }

    // 익월 사용금액 가져오기 -> 한 건당 0.5원
    public int emailSendMonthPrice(String cpCode, String yyyymm) {
        int result = emailRepository.findByMonthSendPrice(cpCode, yyyymm);
        log.info("총 발송건수 : "+result);
        if(result != 0) {
            return (int) Math.round(result*0.5);
        } else {
            return 0;
        }
    }

}
