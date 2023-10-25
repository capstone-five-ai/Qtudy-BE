package com.app.domain.file.summary.service;

import com.app.domain.file.summary.dto.SummaryDto;
import com.app.domain.file.summary.entity.SummaryFiles;
import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SummaryService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> generateSummary(SummaryDto summaryDto) {

        SummaryFiles summaryFiles = summaryDto.toEntity();

        String url = "http://localhost:7000/summary";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = String.format("{\"text\": \"%s\"}", summaryDto.getText()); //json으로 text 전달
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        String result = restTemplate.postForObject(url, request, String.class);
        if(result == null)
            throw new BusinessException(ErrorCode.NOT_GENERATE_SUMMARY);

        return ResponseEntity.ok("Success");

    }
}
