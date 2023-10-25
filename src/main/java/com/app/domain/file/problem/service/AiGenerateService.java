package com.app.domain.file.problem.service;

import com.app.domain.file.problem.dto.AiGenerateDto;
import com.app.domain.file.problem.entity.ProblemFiles;
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
public class AiGenerateService {

    @Autowired
    private RestTemplate restTemplate;


    public ResponseEntity<String> AiGenerateProblem(AiGenerateDto aiGenerateDto) {
        String url;

        ProblemFiles problemFiles = aiGenerateDto.toEntity(); // DTO -> ENTITY 변환

        switch (problemFiles.getProblemType()) {  //Problem 타입 체크
            case MCQ:
                url = "http://localhost:7000/mcq";
                break;
            case SAQ:
                url = "http://localhost:7000/saq";
                break;
            default:
                System.out.println("오류 발생");  // 추후 수정해야 함
                return null; // 오류를 반환하는 방식에 따라 이 부분을 수정할 수 있습니다.
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = String.format("{\"text\": \"%s\"}", aiGenerateDto.getText());
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        String result = restTemplate.postForObject(url, request, String.class);
        if (result == null)// GPT 문제 생성 검사
            throw new BusinessException(ErrorCode.NOT_GENERATE_PROBLEM);


        return ResponseEntity.ok("Success");
    }
}