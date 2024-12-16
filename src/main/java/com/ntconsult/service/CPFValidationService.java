package com.ntconsult.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CPFValidationService {

    @Value("${external.cpf.validation.url}")
    private String validationUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean canVote(String cpf) {
        try {
            String url = validationUrl + cpf;
            var response = restTemplate.getForObject(url, CPFValidationResponse.class);
            return response != null && "ABLE_TO_VOTE".equals(response.getStatus());
        } catch (Exception e) {
            return false;
        }
    }

    private static class CPFValidationResponse {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
