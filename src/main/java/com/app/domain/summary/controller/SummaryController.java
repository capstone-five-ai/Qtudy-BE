package com.app.domain.summary.controller;

import com.app.domain.summary.dto.Summary.Response.SummaryResponseDto;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    SummaryService summaryService;


    @GetMapping("/getSummary/{fileId}") // 요점정리 가져오기
    public ResponseEntity<SummaryResponseDto> GetSummary(HttpServletRequest httpServletRequest, @PathVariable int fileId) {
        AiGeneratedSummary aiGeneratedSummary = summaryService.GetSummary(fileId);

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        Boolean isWriter = false;
        // "Authorization" 헤더가 존재하면 checkIsWriter 함수 호출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            isWriter = summaryService.checkIsWriter(httpServletRequest,fileId); // 인증 변수추가
        }

        //boolean isWriter = summaryService.checkIsWriter(httpServletRequest,fileId);

        SummaryResponseDto responseDto = SummaryResponseDto.ConvertToSummary(isWriter, aiGeneratedSummary);

        return ResponseEntity.ok(responseDto);
    }






}
