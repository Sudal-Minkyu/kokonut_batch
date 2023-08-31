package com.app.batch.utils;

import com.app.batch.email.dtos.EmailCheckDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Service
public class NaverCloudPlatformService {

    @Value("${kokonut.ncloud.serviceId}")
    public String serviceId;

    @Value("${kokonut.ncloud.accessKey}")
    public String accessKey;

    @Value("${kokonut.ncloud.secretKey}")
    public String secretKey;

    @Value("${kokonut.ncloud.primaryKey}")
    public String primaryKey;

    @Value("${kokonut.ncloud.categoryCode}")
    public String categoryCode;

    // 발송된 이메일 상태 체크호출(API : getMailRequestStatus)
    public EmailCheckDto sendEmailCheck(String requestId) {

        EmailCheckDto emailCheckDto = null;

        String url = "https://mail.apigw.ntruss.com/api/v1/mails/requests/"+requestId+"/status";
        log.info("url : "+url);

        try {
            URL apiurl = new URL(url);

            long currentTime = System.currentTimeMillis();
            String currentTimeStr = Long.toString(currentTime);

            String signature = makeSignature(currentTimeStr, "/api/v1/mails/requests/"+requestId+"/status", "GET");

            HttpURLConnection conn = (HttpURLConnection) apiurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("x-ncp-apigw-timestamp", currentTimeStr);
            conn.setRequestProperty("x-ncp-iam-access-key", accessKey);
            conn.setRequestProperty("x-ncp-apigw-signature-v2", signature);
            conn.setUseCaches(false);
            conn.setDoOutput(false);
            conn.setDoInput(true);

            // 응답 데이터 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String code = String.valueOf(conn.getResponseCode());
            String data = sb.toString();

            br.close();
            conn.disconnect();

            log.info("code: {}, data: {}", code, data);

            if(code.equals("200")) {
                emailCheckDto = new EmailCheckDto();

                JSONObject jsonObj = new JSONObject(data);

                int requestCount = jsonObj.getInt("requestCount");
                int sentCount = jsonObj.getInt("sentCount");

                emailCheckDto.setRequestCount(requestCount);
                emailCheckDto.setSentCount(sentCount);
                emailCheckDto.setFailCount(requestCount-sentCount);
            }


        }
        catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return emailCheckDto;
    }

    // 시그네처 생성 함수
    public String makeSignature(String timestamp, String url, String method) {
        String space = " ";  // 공백
        String newLine = "\n";  // 줄바꿈

        String encodeBase64String = "";

        String message = method +
                space +
                url +
                newLine +
                timestamp +
                newLine +
                accessKey;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac;
            mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return encodeBase64String;
    }

}
