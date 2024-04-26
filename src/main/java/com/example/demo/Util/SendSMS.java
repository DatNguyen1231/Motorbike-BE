package com.example.demo.Util;

import com.example.demo.model.Dto.sms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class SendSMS {
    @Value("${sms.api.url}")
    private static String smsApiUrl;

    public static ResponseEntity<?> sendSms(sms smsRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<sms> requestEntity = new HttpEntity<>(smsRequest, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                smsApiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }
}
