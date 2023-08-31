package com.app.batch.utils;

import com.app.batch.email.dtos.EmailCheckDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailSender {

    @Value("${kokonut.mail.host}")
    public String mailHost; // 보내는 사람의 이메일(contact@kokonut.me)

    private final NaverCloudPlatformService naverCloudPlatformService;

    @Autowired
    public MailSender(NaverCloudPlatformService naverCloudPlatformService) {
        this.naverCloudPlatformService = naverCloudPlatformService;
    }

    // 발송된 이메일 상태 체크호출
    public EmailCheckDto sendEmailCheck(String requestId) {
        log.info("sendEmailCheck 호출");
        return naverCloudPlatformService.sendEmailCheck(requestId);
    }

}