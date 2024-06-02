package com.app.domain.summary.aigeneratedsummary.controller;

import com.app.domain.summary.aigeneratedsummary.doc.AiGeneratedSummaryApi;
import com.app.domain.summary.aigeneratedsummary.dto.Summary.Response.SummaryResponseDto;
import com.app.domain.summary.aigeneratedsummary.entity.AiGeneratedSummary;
import com.app.domain.summary.aigeneratedsummary.service.AiGeneratedSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/summary")
public class AiGeneratedSummaryController implements AiGeneratedSummaryApi {

    @Autowired
    AiGeneratedSummaryService aiGeneratedSummaryService;

    @Override
    @GetMapping("/getSummary/{fileId}") // 요점정리 가져오기
    public ResponseEntity<SummaryResponseDto> GetSummary(
        @PathVariable Long fileId,
        HttpServletRequest httpServletRequest) {

        AiGeneratedSummary aiGeneratedSummary = aiGeneratedSummaryService.GetSummary(fileId);

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        Boolean isWriter = false;
        // "Authorization" 헤더가 존재하면 checkIsWriter 함수 호출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            isWriter = aiGeneratedSummaryService.checkIsWriter(httpServletRequest, fileId); // 인증 변수 추가
        }

        SummaryResponseDto responseDto = SummaryResponseDto.ConvertToSummary(isWriter, aiGeneratedSummary);

        return ResponseEntity.ok(responseDto);
    }
}
