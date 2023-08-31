package com.app.batch.awskmshistory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Woody
 * Date : 2023-08-11
 * Time :
 * Remark : AwsKmsHistoryService
 */
@Slf4j
@Service
public class AwsKmsHistoryService {

    private final AwsKmsHistoryRepository awsKmsHistoryRepository;

    @Autowired
    public AwsKmsHistoryService(AwsKmsHistoryRepository awsKmsHistoryRepository) {
        this.awsKmsHistoryRepository = awsKmsHistoryRepository;
    }

    // AWS 호출금액 -> 한 건당 0.03$
    public double findByMonthKmsPrice(String cpCode, String akhYyyymm) {
        log.info("findByMonthKmsPrice 호출");

        Long result = awsKmsHistoryRepository.findByMonthKmsPrice(cpCode, akhYyyymm);
        log.info("KMS 총 호출 수 : "+result);
        if(result != 0) {
            return result*0.03;
        } else {
            return 0;
        }
    }

}
