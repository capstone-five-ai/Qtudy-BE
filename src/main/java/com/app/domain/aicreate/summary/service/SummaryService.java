package com.app.domain.aicreate.summary.service;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

public class SummaryService {

    public String generateSummary(String text) throws IOException {
        WebClient webClient = WebClient.create();
        String url = "http://localhost:7000/summary";
        String jsonBody = String.format("{\"text\": \"%s\"}", text);

        return webClient.post()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
