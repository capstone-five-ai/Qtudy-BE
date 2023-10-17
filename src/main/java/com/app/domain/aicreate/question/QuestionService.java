package com.app.domain.aicreate.question;

import com.app.domain.aicreate.ENUM.GptType;
import com.example.demo.aicreate.ENUM.GptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Service
public class QuestionService {
    @Autowired
    private RestTemplate restTemplate;


    public String createQuestion(QuestionDto qeustionDto) {
        String url = null;

        switch (type) {
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

        String jsonBody = String.format("{\"text\": \"%s\"}", text);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        return restTemplate.postForObject(url, request, String.class);
    }
}