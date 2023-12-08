package com.app.domain.summary.controller;

import com.app.domain.summary.dto.Summary.Response.GetSummaryResponseDto;
import com.app.domain.summary.entity.AiGeneratedSummary;
import com.app.domain.summary.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    SummaryService summaryService;


    @GetMapping("/getSummary/{aiGeneratedSummaryId}") // 요점정리 가져오기
    public ResponseEntity<GetSummaryResponseDto> GetSummary(@PathVariable int aiGeneratedSummaryId) {
        AiGeneratedSummary aiGeneratedSummary = summaryService.GetSummary(aiGeneratedSummaryId);

        GetSummaryResponseDto responseDto = GetSummaryResponseDto.ConvertToSummary(aiGeneratedSummary);



        return ResponseEntity.ok(responseDto);
    }






}
