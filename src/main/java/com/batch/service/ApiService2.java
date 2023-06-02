package com.batch.service;

import com.batch.domain.ApiInfo;
import com.batch.domain.ApiResponseV0;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService2 extends AbstractApiService {
    
    @Override
    protected ApiResponseV0 doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8880/api/product/2", apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        ApiResponseV0 apiResponseV0 = ApiResponseV0.builder()
                .status(statusCodeValue)
                .msg(responseEntity.getBody())
                .build();
        return apiResponseV0;
    }
}
