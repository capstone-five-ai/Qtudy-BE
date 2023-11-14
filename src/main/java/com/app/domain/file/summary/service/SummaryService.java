package com.app.domain.file.summary.service;

import com.app.domain.file.summary.dto.SummaryDto;
import com.app.domain.file.summary.entity.SummaryFiles;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SummaryService {
    String url ;
    String content;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> generateSummary(SummaryDto summaryDto) {


        HttpHeaders headers = new HttpHeaders();  // HTTP 요청 헤더 설정
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();  // JSON 데이터로 변환하기 위한 ObjectMapper 생성

        String jsonBody;   // AI 문제 DTO를 JSON 문자열로 변환
        try {
            jsonBody = objectMapper.writeValueAsString(summaryDto);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.NOT_GENERATE_SUMMARY);
        }

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers); // // HTTP 요청 전송

        try {
            content = restTemplate.postForObject(url, request, String.class); // http 응답 받아옴
        } catch (RestClientException e) { // 응답 제대로 안왔을 시, 에러처리
            throw new BusinessException(ErrorCode.NOT_GENERATE_SUMMARY);
        }


        return ResponseEntity.ok("Success");

    }
}
